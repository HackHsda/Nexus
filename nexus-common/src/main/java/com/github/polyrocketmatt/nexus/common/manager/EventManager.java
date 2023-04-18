package com.github.polyrocketmatt.nexus.common.manager;

import com.github.polyrocketmatt.nexus.api.events.NexusListener;
import com.github.polyrocketmatt.nexus.api.manager.NexusManager;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

import java.util.HashSet;
import java.util.Set;

public class EventManager implements NexusManager {

    private final Set<NexusListener> listeners;

    public EventManager() {
        this.listeners = new HashSet<>();
    }

    @Override
    public void close() {
        this.listeners.clear();

        NexusLogger.inform("Closed %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
    }

    public void registerListener(NexusListener listener) {
        this.listeners.add(listener);

        NexusLogger.inform("Registered listener: %s".formatted(listener.getClass().getSimpleName()), NexusLogger.LogType.COMMON);
    }

}
