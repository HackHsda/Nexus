package com.github.polyrocketmatt.nexus.paper.handlers;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.MinecraftKey;
import com.github.polyrocketmatt.nexus.api.PlatformType;
import com.github.polyrocketmatt.nexus.api.entity.NexusEntity;
import com.github.polyrocketmatt.nexus.api.events.NexusEvent;
import com.github.polyrocketmatt.nexus.api.module.NexusModuleType;
import com.github.polyrocketmatt.nexus.common.entity.NexusPlayer;
import com.github.polyrocketmatt.nexus.common.modules.ClientDetectionModule;
import com.github.polyrocketmatt.nexus.common.modules.ModuleHandler;
import com.github.polyrocketmatt.nexus.common.utils.Pair;
import com.github.polyrocketmatt.nexus.paper.PaperNexus;
import com.github.polyrocketmatt.nexus.paper.events.nexus.PlayerPacketEvent;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.Set;

public class PaperCustomPayloadHandler extends ModuleHandler {

    private final String clientDetectedMessage = PaperNexus.getInstance().getConfiguration().getString("messages.client-detected");
    private final String modDetectedMessage = PaperNexus.getInstance().getConfiguration().getString("messages.mod-detected");
    private final boolean checkMods = PaperNexus.getInstance().getConfiguration().getBoolean("modules.client-detection.mod-options.check-mods");

    public PaperCustomPayloadHandler() {
        super(NexusModuleType.CLIENT_DETECTION);
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.PAPER;
    }

    @Override
    public void process(NexusEvent nexusEvent, NexusEntity entity) {
        if (!(nexusEvent instanceof PlayerPacketEvent event))
            return;
        if (!(entity instanceof NexusPlayer player))
            return;
        String channel = event.getChannel();
        String message = event.getMessage();

        //  Pass the packet information to the client detection module
        Pair<Boolean, String> detectionResult = PaperNexus.getInstance().<ClientDetectionModule>getModule(NexusModuleType.CLIENT_DETECTION)
                .verify(channel, message);

        if (!detectionResult.getFirst())
            player.sendMessage(clientDetectedMessage.formatted(detectionResult.getSecond()));

        //  Handle forge detection
        if (checkMods)
            handleMods(player, message);
    }

    private void handleMods(NexusPlayer player, String message) {
        Set<String> mods = PaperNexus.getInstance().<ClientDetectionModule>getModule(NexusModuleType.CLIENT_DETECTION).parseForgeMods(message);

        mods.forEach(mod -> player.sendMessage(modDetectedMessage.formatted(mod)));
    }

}
