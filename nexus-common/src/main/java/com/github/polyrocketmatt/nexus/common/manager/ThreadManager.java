package com.github.polyrocketmatt.nexus.common.manager;

import com.github.polyrocketmatt.nexus.api.manager.NexusManager;
import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.exception.NexusThreadingException;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.lang.Math.min;

public class ThreadManager extends Thread implements NexusManager {

    private final Set<ThreadPoolExecutor> services;
    private final int maxThreadCount;
    private final long maxDefaultWait;

    private int availableThreads;

    public ThreadManager() {
        this.services = new HashSet<>();
        this.maxThreadCount = Integer.parseInt(Nexus.getProperties().getProperty("threading.pool-size"));
        this.maxDefaultWait = Long.parseLong(Nexus.getProperties().getProperty("threading.max-default-wait"));
        this.availableThreads = maxThreadCount;

        NexusLogger.inform("Thread manager initialised on thread %s", NexusLogger.LogType.COMMON, Thread.currentThread().getName());
        NexusLogger.inform("    Thread pool size: %s", NexusLogger.LogType.COMMON, maxThreadCount);
    }

    public ExecutorService getService(int threadCount) {
        int grantedThreads = min(threadCount, availableThreads);

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(grantedThreads);
        availableThreads -= grantedThreads;
        services.add(executor);

        return executor;
    }

    public void handleTermination(ExecutorService executorService, long maxWait) throws NexusThreadingException {
        if (!(executorService instanceof ThreadPoolExecutor service))
            throw new NexusThreadingException("Service is not a thread pool executor");
        if (!services.contains(service))
            throw new NexusThreadingException("Service not registered with manager");
        try {
            service.shutdown();
            boolean terminated = service.awaitTermination(maxWait, TimeUnit.MILLISECONDS);
            if (!terminated)
                throw new NexusThreadingException("Service did not terminate within the specified time");

            //  Return the threads to the pool
            availableThreads += service.getPoolSize();
        } catch (InterruptedException ex) {
            throw new NexusThreadingException("Service interrupted while waiting for termination");
        }
    }

    @Override
    public void close() {
        int serviceCount = services.size();
        services.forEach(service -> handleTermination(service, maxDefaultWait));

        NexusLogger.inform("Closed %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
        NexusLogger.inform("    Forced Shutdown Count: %s", NexusLogger.LogType.COMMON, serviceCount);
    }

    public <T> void accept(Supplier<T> supplier, Consumer<T> consumer) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) getService(1);
        CompletableFuture
                .supplyAsync(supplier, executor)
                .thenAccept(consumer);
        handleTermination(executor, maxDefaultWait);
    }

    public <T> void accept(Supplier<T> supplier, Consumer<T> consumer, long wait) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) getService(1);
        CompletableFuture
                .supplyAsync(supplier, executor)
                .thenAccept(consumer);
        handleTermination(executor, wait);
    }


}
