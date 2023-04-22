package com.github.polyrocketmatt.nexus.paper.handlers;

import com.github.polyrocketmatt.nexus.api.PlatformType;
import com.github.polyrocketmatt.nexus.api.modules.ModuleProcessResult;
import com.github.polyrocketmatt.nexus.api.modules.NexusModuleType;
import com.github.polyrocketmatt.nexus.common.modules.ModuleHandler;
import com.github.polyrocketmatt.nexus.common.modules.result.ModDetectionResult;
import com.github.polyrocketmatt.nexus.paper.PaperNexus;

public class PaperModDetectionHandler extends ModuleHandler {

    private final String modDetectedMessage = PaperNexus.getInstance().getMessages().getString("mod-detected");

    public PaperModDetectionHandler() {
        super(NexusModuleType.MOD_DETECTION);
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.PAPER;
    }

    @Override
    public void handle(ModuleProcessResult processResult) {
        if (!(processResult instanceof ModDetectionResult result))
            return;

        if (result.isFailed())
            result.getMods().forEach(mod -> result.getEntity().sendMessage(modDetectedMessage.formatted(mod)));
    }

}
