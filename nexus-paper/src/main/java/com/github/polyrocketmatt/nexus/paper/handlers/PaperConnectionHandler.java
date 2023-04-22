package com.github.polyrocketmatt.nexus.paper.handlers;

import com.github.polyrocketmatt.nexus.api.PlatformType;
import com.github.polyrocketmatt.nexus.api.modules.ModuleProcessResult;
import com.github.polyrocketmatt.nexus.api.modules.NexusModuleType;
import com.github.polyrocketmatt.nexus.common.modules.ModuleHandler;
import com.github.polyrocketmatt.nexus.common.modules.result.ConnectionDetectionResult;
import com.github.polyrocketmatt.nexus.paper.PaperNexus;

public class PaperConnectionHandler extends ModuleHandler {

    private final String ipDetectedMessage = PaperNexus.getInstance().getMessages().getString("ip-detected");

    public PaperConnectionHandler() {
        super(NexusModuleType.CLIENT_CONNECTION);
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.PAPER;
    }

    @Override
    public void handle(ModuleProcessResult processResult) {
        if (!(processResult instanceof ConnectionDetectionResult result))
            return;

        if (result.isFailed()) {
            String typeString = result.getConnectionTypes().stream().reduce("", (s, type) -> s + type.name() + ", ", String::concat);

            //  Remove the last comma and space
            typeString = typeString.substring(0, typeString.length() - 2);
            result.getEntity().sendMessage(ipDetectedMessage.formatted(typeString));
        }
    }
}
