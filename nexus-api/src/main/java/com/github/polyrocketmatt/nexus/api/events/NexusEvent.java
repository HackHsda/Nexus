package com.github.polyrocketmatt.nexus.api.events;

import com.github.polyrocketmatt.nexus.api.module.NexusModuleType;

import java.util.UUID;

public abstract class NexusEvent {

    private final UUID uuid;

    public NexusEvent(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public abstract NexusModuleType getModuleHandle();

}
