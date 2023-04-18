package com.github.polyrocketmatt.nexus.paper.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;

public abstract class PaperProtocol {

    private final PacketType[] types;

    public PaperProtocol(PacketType... types) {
        this.types = types;
    }

    public abstract PacketAdapter getAdapter();

    public boolean hasType(PacketType type) {
        for (PacketType t : types)
            if (t == type)
                return true;
        return false;
    }

}
