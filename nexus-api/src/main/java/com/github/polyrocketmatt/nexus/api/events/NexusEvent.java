package com.github.polyrocketmatt.nexus.api.events;

import java.util.UUID;

public abstract class NexusEvent implements InternalNexusEvent {

    private final UUID uuid;

    public NexusEvent(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUniqueId() {
        return uuid;
    }

}
