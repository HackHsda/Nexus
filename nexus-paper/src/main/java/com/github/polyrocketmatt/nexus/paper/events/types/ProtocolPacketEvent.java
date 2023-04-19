package com.github.polyrocketmatt.nexus.paper.events.types;

import com.comphenix.protocol.events.PacketEvent;
import com.github.polyrocketmatt.nexus.api.events.NexusEvent;
import com.github.polyrocketmatt.nexus.api.module.NexusModuleType;

public class ProtocolPacketEvent implements NexusEvent {

    private final PacketEvent event;

    public ProtocolPacketEvent(PacketEvent event) {
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
