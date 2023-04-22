package com.github.polyrocketmatt.nexus.paper.events.packets.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.MinecraftKey;
import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.paper.PaperNexus;
import com.github.polyrocketmatt.nexus.paper.events.packets.PaperPacketListener;
import com.github.polyrocketmatt.nexus.common.event.PlayerPacketEvent;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
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
                PacketContainer packet = event.getPacket();
                StructureModifier<MinecraftKey> keys = packet.getMinecraftKeys();
                StructureModifier<Object> modifier = packet.getModifier();
                String channel = keys.read(0).getFullKey();
                String message = ((ByteBuf) modifier.read(1)).toString(StandardCharsets.UTF_8);
                PlayerPacketEvent nexusEvent = new PlayerPacketEvent(uuid, channel, message);
                Nexus.getEventManager().dispatch(nexusEvent);
            }
        };
    }

    @Override
    public PacketAdapter getAdapter() {
        return adapter;
    }

}
