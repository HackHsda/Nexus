package com.github.polyrocketmatt.nexus.paper.events.packets.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.paper.PaperNexus;
import com.github.polyrocketmatt.nexus.paper.events.packets.PaperPacketListener;
import com.github.polyrocketmatt.nexus.paper.events.nexus.PlayerPacketEvent;

import java.util.UUID;

public class PaperCustomPayloadListener extends PaperPacketListener {

    private final PacketAdapter adapter;

    public PaperCustomPayloadListener() {
        super(PacketType.Play.Client.CUSTOM_PAYLOAD);

        this.adapter = new PacketAdapter(
                PaperNexus.getInstance(),
                ListenerPriority.HIGHEST,
                PacketType.Play.Client.CUSTOM_PAYLOAD
        ) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                UUID uuid = event.getPlayer().getUniqueId();
                PlayerPacketEvent nexusEvent = new PlayerPacketEvent(uuid, event);
                Nexus.getEventManager().dispatch(nexusEvent);
            }
        };
    }

    @Override
    public PacketAdapter getAdapter() {
        return adapter;
    }

}
