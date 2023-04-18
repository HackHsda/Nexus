package com.github.polyrocketmatt.nexus.common.manager;

import com.github.polyrocketmatt.nexus.api.manager.NexusManager;
import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ThreadManager extends Thread implements NexusManager {

    private final ExecutorService executorService;

    public ThreadManager() {
        int threadCount = Integer.parseInt(Nexus.getProperties().getProperty("threading.pool-size"));
        this.executorService = Executors.newFixedThreadPool(threadCount);

        NexusLogger.inform("Thread manager initialised on thread %s", NexusLogger.LogType.COMMON, Thread.currentThread().getName());
        NexusLogger.inform("    Thread pool size: %s", NexusLogger.LogType.COMMON, threadCount);
    }

    @Override
    public void close() {
        List<Runnable> runningTasks = executorService.shutdownNow();

        NexusLogger.inform("Closed %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
        NexusLogger.inform("    Forced Shutdown Count: %s", NexusLogger.LogType.COMMON, runningTasks.size());
    }

    public <T> void accept(Supplier<T> supplier, Consumer<T> consumer) {
        CompletableFuture.supplyAsync(supplier).thenAccept(consumer);
    }

}
