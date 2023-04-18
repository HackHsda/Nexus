package com.github.polyrocketmatt.nexus.common.manager;

import com.github.polyrocketmatt.nexus.api.events.NexusListener;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

import java.util.HashSet;
import java.util.Set;

public class EventManager {

    private Set<NexusListener> listeners;

    public EventManager() {
        this.listeners = new HashSet<>();
    }

    public void registerListener(NexusListener listener) {
        this.listeners.add(listener);

        NexusLogger.inform("Registered listener: %s".formatted(listener.getClass().getSimpleName()), NexusLogger.LogType.COMMON);
    }

}
