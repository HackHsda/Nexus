package com.github.polyrocketmatt.nexus.common.event;

import com.github.polyrocketmatt.nexus.api.events.NexusEvent;

import java.util.UUID;

public class PlayerPacketEvent extends NexusEvent {

    private final String channel;
    private final String message;

    public PlayerPacketEvent(UUID uuid, String channel, String message) {
        super(uuid);
        this.channel = channel;
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }
}
