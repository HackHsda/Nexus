package com.github.polyrocketmatt.nexus.common.manager;

import com.github.polyrocketmatt.nexus.api.manager.NexusManager;
import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.exception.NexusThreadingException;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.lang.Math.min;

public class ThreadManager implements NexusManager {

    private final Map<ThreadPoolExecutor, Integer> services;
    private final int maxThreadCount;
    private final long maxDefaultWait;

    private int availableThreads;

    public ThreadManager() {
        this.services = new HashMap<>();
        this.maxThreadCount = Integer.parseInt(Nexus.getProperties().getProperty("threading.pool-size"));
        this.maxDefaultWait = Long.parseLong(Nexus.getProperties().getProperty("threading.max-default-wait"));
        this.availableThreads = maxThreadCount;

        NexusLogger.inform("Initialised %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
        NexusLogger.inform("    Thread pool size: %s", NexusLogger.LogType.COMMON, maxThreadCount);
    }

    @Override
    public void close() {
        int serviceCount = services.size();
        services.forEach((service, threads) -> handleTermination(service, maxDefaultWait));

        NexusLogger.inform("Closed %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
        NexusLogger.inform("    Forced Shutdown Count: %s", NexusLogger.LogType.COMMON, serviceCount);
    }

    @Override
    public void log() {
        NexusLogger.inform("Thread Manager", NexusLogger.LogType.COMMON);
        NexusLogger.inform("--------------", NexusLogger.LogType.COMMON);
        NexusLogger.inform("    Services: %s", NexusLogger.LogType.COMMON, services.size());
        NexusLogger.inform("    Available Threads: %s", NexusLogger.LogType.COMMON, availableThreads);
        NexusLogger.inform("    Max Thread Count: %s", NexusLogger.LogType.COMMON, maxThreadCount);
        NexusLogger.inform("    Max Default Wait: %s", NexusLogger.LogType.COMMON, maxDefaultWait);
        NexusLogger.inform("", NexusLogger.LogType.COMMON);
    }

    public ExecutorService getService(int threadCount) {
        int grantedThreads = min(threadCount, availableThreads);

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(grantedThreads);
        availableThreads -= grantedThreads;
        services.put(executor, grantedThreads);

        return executor;
    }

    public void handleTermination(ExecutorService executorService, long maxWait) throws NexusThreadingException {
        if (!(executorService instanceof ThreadPoolExecutor service))
            throw new NexusThreadingException("Service is not a thread pool executor");
        if (!services.containsKey(service))
            throw new NexusThreadingException("Service not registered with manager");
        try {
            int threadCount = services.get(service);

            service.shutdown();

            boolean terminated = service.awaitTermination(maxWait, TimeUnit.MILLISECONDS);
            if (!terminated)
                throw new NexusThreadingException("Service did not terminate within the specified time");

            //  Return the threads to the pool
            availableThreads += threadCount;
            services.remove(service);
        } catch (InterruptedException ex) {
            throw new NexusThreadingException("Service interrupted while waiting for termination");
        }
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

    public void submit(Runnable runnable) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) getService(1);
        CompletableFuture
                .runAsync(runnable, executor);
        handleTermination(executor, maxDefaultWait);
    }

    public void submit(Runnable runnable, long wait) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) getService(1);
        CompletableFuture
                .runAsync(runnable, executor);
        handleTermination(executor, wait);
    }


}
