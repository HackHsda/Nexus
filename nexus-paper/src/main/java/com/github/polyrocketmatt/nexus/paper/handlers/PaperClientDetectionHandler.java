package com.github.polyrocketmatt.nexus.paper.handlers;

import com.github.polyrocketmatt.nexus.api.PlatformType;
import com.github.polyrocketmatt.nexus.api.modules.NexusModuleType;
import com.github.polyrocketmatt.nexus.api.modules.ModuleProcessResult;
import com.github.polyrocketmatt.nexus.common.modules.result.ClientDetectionResult;
import com.github.polyrocketmatt.nexus.common.modules.ModuleHandler;
import com.github.polyrocketmatt.nexus.paper.PaperNexus;

public class PaperClientDetectionHandler extends ModuleHandler {

    private final String clientDetectedMessage = PaperNexus.getInstance().getMessages().getString("client-detected");

    public PaperClientDetectionHandler() {
        super(NexusModuleType.CLIENT_DETECTION);
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.PAPER;
    }

    @Override
    public void handle(ModuleProcessResult processResult) {
        if (!(processResult instanceof ClientDetectionResult result))
            return;

        if (result.isFailed())
            result.getEntity().sendMessage(clientDetectedMessage.formatted(result.getClientName()));
    }
}
