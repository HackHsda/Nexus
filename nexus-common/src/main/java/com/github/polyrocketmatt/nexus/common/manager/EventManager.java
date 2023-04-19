package com.github.polyrocketmatt.nexus.common.manager;

import com.github.polyrocketmatt.nexus.api.events.NexusEvent;
import com.github.polyrocketmatt.nexus.api.events.NexusListener;
import com.github.polyrocketmatt.nexus.api.manager.NexusManager;
import com.github.polyrocketmatt.nexus.api.scheduling.NexusTask;
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

    private final Set<NexusListener> listeners;
    private final Map<UUID, Queue<NexusEvent>> eventBox;

    public EventManager() {
        this.listeners = new HashSet<>();
        this.eventBox = new HashMap<>();
    }

    @Override
    public void close() {
        this.listeners.clear();

        NexusLogger.inform("Closed %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
    }

    public void registerListener(NexusListener listener) {
        this.listeners.add(listener);

        NexusLogger.inform("Registered listener: %s", NexusLogger.LogType.COMMON, listener.getClass().getSimpleName());
    }

    public void initialiseMessages(@NotNull UUID uuid) {
        if (eventBox.containsKey(uuid))
            return;
        eventBox.put(uuid, new LinkedList<>());
    }

    public void deleteMessages(@NotNull UUID uuid) {
        if (!eventBox.containsKey(uuid))
            return;
        eventBox.remove(uuid);
    }

    public void enqueue(UUID uuid, NexusEvent event) {
        if (!eventBox.containsKey(uuid))
            throw new NexusEntityException("Player with UUID: %s does not exist!".formatted(uuid));
        eventBox.get(uuid).add(event);

        //  Reschedule processing task
        Nexus.getTaskManager().rescheduleTask(uuid);

        NexusLogger.inform("Enqueued event: %s for UUID %s", NexusLogger.LogType.COMMON, event.getClass().getSimpleName(), uuid.toString());
        NexusLogger.inform("    Event Handle: %s", NexusLogger.LogType.COMMON, event.getModuleHandle());
    }

    public @Nullable NexusEvent deque(UUID uuid) {
        if (!eventBox.containsKey(uuid))
            throw new NexusEntityException("Player with UUID: %s does not exist!".formatted(uuid));
        return eventBox.get(uuid).poll();
    }

    public int getQueueSize(UUID uuid) {
        if (!eventBox.containsKey(uuid))
            throw new NexusEntityException("Player with UUID: %s does not exist!".formatted(uuid));
        return eventBox.get(uuid).size();
    }

}
