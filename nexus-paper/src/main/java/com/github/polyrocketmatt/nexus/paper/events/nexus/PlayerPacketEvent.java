package com.github.polyrocketmatt.nexus.paper.events.nexus;

import com.github.polyrocketmatt.nexus.api.events.NexusEvent;
import com.github.polyrocketmatt.nexus.api.module.NexusModuleType;

import java.util.UUID;

public class PlayerPacketEvent extends NexusEvent {

    private final String channel;
    private final String message;

    public PlayerPacketEvent(UUID uuid, String channel, String message) {
        super(uuid);
        this.channel = channel;
        this.message = message;
    }

    @Override
    public NexusModuleType getModuleHandle() {
        return NexusModuleType.CLIENT_DETECTION;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }
}
