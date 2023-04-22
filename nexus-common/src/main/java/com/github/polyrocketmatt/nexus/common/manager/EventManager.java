package com.github.polyrocketmatt.nexus.common.manager;

import com.github.polyrocketmatt.nexus.api.events.ExternalEventListener;
import com.github.polyrocketmatt.nexus.api.events.InternalEventListener;
import com.github.polyrocketmatt.nexus.api.events.NexusEvent;
import com.github.polyrocketmatt.nexus.api.manager.NexusManager;
import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.exception.NexusProcessingException;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class EventManager implements NexusManager {

    private final Set<ExternalEventListener> externalListeners;
    private final Set<InternalEventListener> internalListeners;
    private final Queue<NexusEvent> eventQueue;
    private final Thread[] workers;
    private final long eventWorkerTimeout;

    public EventManager() {
        int workerCount = Integer.parseInt(Nexus.getProperties().getProperty("threading.event-workers"));
        this.externalListeners = new HashSet<>();
        this.internalListeners = new HashSet<>();
        this.eventQueue = new LinkedList<>();
        this.workers = new Thread[workerCount];
        for (int i = 0; i < workerCount; i++) {
            workers[i] = new Thread(new EventWorker(), "Nexus Worker %s".formatted(i));
            workers[i].start();
        }
        this.eventWorkerTimeout = Long.parseLong(Nexus.getProperties().getProperty("threading.event-worker-timeout"));

        NexusLogger.inform("Initialised %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
    }

    @Override
    public void close() {
        externalListeners.clear();

        NexusLogger.inform("Closed %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
    }

    @Override
    public void log() {
        NexusLogger.inform("Event Manager", NexusLogger.LogType.COMMON);
        NexusLogger.inform("-------------", NexusLogger.LogType.COMMON);
        NexusLogger.inform("    External Listeners: %s", NexusLogger.LogType.COMMON, externalListeners.size());
        NexusLogger.inform("    Event Queues: %s", NexusLogger.LogType.COMMON, eventQueue.size());
        NexusLogger.inform("    Workers: %s", NexusLogger.LogType.COMMON, workers.length);
        NexusLogger.inform("", NexusLogger.LogType.COMMON);
    }

    public void registerExternalListener(@NotNull ExternalEventListener listener) {
        externalListeners.add(listener);
    }

    public void registerInternalListener(@NotNull InternalEventListener listener) {
        internalListeners.add(listener);
    }

    public synchronized void dispatch(@NotNull NexusEvent event) {
        eventQueue.offer(event);
        internalListeners.forEach(listener -> listener.handle(event));
        notifyAll();
    }

    private class EventWorker implements Runnable {

        @Override
        public void run() {
            while (true) {
                //  Acquire lock on event queue
                synchronized (EventManager.this) {
                    while (eventQueue.isEmpty()) {
                        try {
                            EventManager.this.wait();
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();

                            return;
                        }
                    }

                    var event = eventQueue.poll();

                    NexusLogger.inform("Worker %s is processing event %s", NexusLogger.LogType.COMMON, Thread.currentThread().getName(), event.getClass().getSimpleName());

                    var player = Nexus.getPlayerManager().getPlayer(event.getUniqueId());
                    var module = Nexus.getModuleManager().getModule(event.getModuleHandle());
                    if (player == null || module == null || player.getPlayerData() == null) {
                        try {
                            Thread.sleep(eventWorkerTimeout);

                            dispatch(event);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                            NexusLogger.warn("Worker %s interrupted!", NexusLogger.LogType.COMMON, Thread.currentThread().getName());

                            return;
                        }

                        NexusLogger.warn("Worker %s could not find player or module for event %s", NexusLogger.LogType.COMMON, Thread.currentThread().getName(), event.getClass().getSimpleName());

                        return;
                    }

                    var handler = module.getModuleHandler(Nexus.getPlatform().getPlatformType());
                    var result = module.process(player, event);
                    if (handler != null)
                        try {
                            handler.handle(result);
                        } catch (Exception ex) {
                            throw new NexusProcessingException("Could not process event %s".formatted(event.getClass().getSimpleName()), ex);
                        }
                }
            }
        }

    }

}
