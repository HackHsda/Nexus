package com.github.polyrocketmatt.nexus.common.manager;

import com.github.polyrocketmatt.nexus.api.events.InternalEventListener;
import com.github.polyrocketmatt.nexus.api.events.ExternalEventListener;
import com.github.polyrocketmatt.nexus.api.events.NexusEvent;
import com.github.polyrocketmatt.nexus.api.manager.NexusManager;
import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.exception.NexusEntityException;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

public class EventManager implements NexusManager {

    private final Set<ExternalEventListener> externalListeners;
    private final Set<InternalEventListener> internalListeners;
    private final Map<UUID, Queue<NexusEvent>> eventQueues;

    public EventManager() {
        this.externalListeners = new HashSet<>();
        this.internalListeners = new HashSet<>();
        this.eventQueues = new HashMap<>();

        NexusLogger.inform("Initialised %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
    }

    @Override
    public void close() {
        this.externalListeners.clear();
        this.internalListeners.clear();

        NexusLogger.inform("Closed %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
    }

    public void registerInternalListener(@NotNull InternalEventListener listener) {
        this.internalListeners.add(listener);

        NexusLogger.inform("Registered internal listener: %s", NexusLogger.LogType.COMMON, listener.getClass().getSimpleName());
    }

    public void registerExternalListener(@NotNull ExternalEventListener listener) {
        this.externalListeners.add(listener);

        NexusLogger.inform("Registered external listener: %s", NexusLogger.LogType.COMMON, listener.getClass().getSimpleName());
    }

    public void dispatch(@NotNull NexusEvent event) {
        Nexus.getThreadManager().submit(() -> {
            internalListeners.forEach(listener -> listener.handle(event));
            enqueue(event.getUniqueId(), event);
        });
    }

    public void initialiseEventQueue(@NotNull UUID uuid) {
        if (eventQueues.containsKey(uuid))
            return;
        eventQueues.put(uuid, new LinkedList<>());
    }

    public void removeEventQueue(@NotNull UUID uuid) {
        if (!eventQueues.containsKey(uuid))
            return;
        eventQueues.remove(uuid);
    }

    public void enqueue(@NotNull UUID uuid, @NotNull NexusEvent event) {
        if (!eventQueues.containsKey(uuid))
            throw new NexusEntityException("Player with UUID: %s does not exist!".formatted(uuid));
        eventQueues.get(uuid).add(event);

        //  Reschedule processing task
        if (!Nexus.getTaskManager().taskIsRunning(uuid))
            Nexus.getTaskManager().rescheduleTask(uuid);
        NexusLogger.inform("Enqueued event: %s for UUID %s", NexusLogger.LogType.COMMON, event.getClass().getSimpleName(), uuid.toString());
        NexusLogger.inform("    Event Handle: %s", NexusLogger.LogType.COMMON, event.getModuleHandle());
    }

    public @Nullable NexusEvent deque(@NotNull UUID uuid) {
        if (!eventQueues.containsKey(uuid))
            throw new NexusEntityException("Player with UUID: %s does not exist!".formatted(uuid));
        return eventQueues.get(uuid).poll();
    }

    public int getQueueSize(@NotNull UUID uuid) {
        if (!eventQueues.containsKey(uuid))
            throw new NexusEntityException("Player with UUID: %s does not exist!".formatted(uuid));
        return eventQueues.get(uuid).size();
    }

}
