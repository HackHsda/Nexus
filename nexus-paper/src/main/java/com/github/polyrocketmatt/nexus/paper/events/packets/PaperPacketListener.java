package com.github.polyrocketmatt.nexus.paper.events.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.github.polyrocketmatt.nexus.api.events.ExternalEventListener;
import com.github.polyrocketmatt.nexus.common.Nexus;

public abstract class PaperPacketListener implements ExternalEventListener {

    private final PacketType[] types;

    public PaperPacketListener(PacketType... types) {
        this.types = types;

        Nexus.getEventManager().registerExternalListener(this);
    }

    public abstract PacketAdapter getAdapter();

    public boolean hasType(PacketType type) {
        for (PacketType t : types)
            if (t == type)
                return true;
        return false;
    }

}
