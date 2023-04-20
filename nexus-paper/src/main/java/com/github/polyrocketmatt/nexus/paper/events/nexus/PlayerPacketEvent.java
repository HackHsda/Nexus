package com.github.polyrocketmatt.nexus.paper.events.nexus;

import com.comphenix.protocol.events.PacketEvent;
import com.github.polyrocketmatt.nexus.api.events.NexusEvent;
import com.github.polyrocketmatt.nexus.api.module.NexusModuleType;

import java.util.UUID;

public class PlayerPacketEvent extends NexusEvent {

    private final PacketEvent event;

    public PlayerPacketEvent(UUID uuid, PacketEvent event) {
        super(uuid);
        this.event = event;
    }

    @Override
    public NexusModuleType getModuleHandle() {
        return NexusModuleType.CLIENT_DETECTION;
    }

    public PacketEvent getEvent() {
        return event;
    }
}
