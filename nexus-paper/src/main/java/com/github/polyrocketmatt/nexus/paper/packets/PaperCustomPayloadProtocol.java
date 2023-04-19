package com.github.polyrocketmatt.nexus.paper.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.MinecraftKey;
import com.github.polyrocketmatt.nexus.api.module.NexusModuleType;
import com.github.polyrocketmatt.nexus.common.entity.NexusPlayer;
import com.github.polyrocketmatt.nexus.common.modules.ClientDetectionModule;
import com.github.polyrocketmatt.nexus.common.utils.Pair;
import com.github.polyrocketmatt.nexus.paper.PaperNexus;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class PaperCustomPayloadProtocol extends PaperProtocol {

    private final PacketAdapter adapter;
    private final String clientDetectedMessage = PaperNexus.getInstance().getConfiguration().getString("messages.client-detected");

    public PaperCustomPayloadProtocol() {
        super(PacketType.Play.Client.CUSTOM_PAYLOAD);
        this.adapter = new PacketAdapter(
                PaperNexus.getInstance(),
                ListenerPriority.HIGHEST,
                PacketType.Play.Client.CUSTOM_PAYLOAD
        ) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                NexusPlayer player = PaperNexus.getInstance().getPlayer(event.getPlayer().getUniqueId());
                PacketContainer packet = event.getPacket();
                StructureModifier<MinecraftKey> keys = packet.getMinecraftKeys();
                StructureModifier<Object> modifier = packet.getModifier();
                String channel = keys.read(0).getFullKey();
                String message = ((ByteBuf) modifier.read(1)).toString(StandardCharsets.UTF_8);

                System.out.println("Channel: " + channel);
                System.out.println("Message: " + message);

                //  Pass the packet information to the client detection module
                Pair<Boolean, String> result = PaperNexus.getInstance().<ClientDetectionModule>getModule(NexusModuleType.CLIENT_DETECTION)
                        .verify(channel, message);

                if (!result.getFirst())
                    player.sendMessage(clientDetectedMessage.formatted(result.getSecond()));
            }
        };
    }

    @Override
    public PacketAdapter getAdapter() {
        return adapter;
    }

}
