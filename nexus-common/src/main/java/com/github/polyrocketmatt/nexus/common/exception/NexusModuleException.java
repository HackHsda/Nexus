package com.github.polyrocketmatt.nexus.common.exception;

import com.github.polyrocketmatt.nexus.api.module.NexusModuleType;
import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

public class NexusModuleException extends NexusException {

    public NexusModuleException(String message, NexusModuleType type) {
        super(message);

        if (!Nexus.getPlatform().getConfiguration().getBoolean("logging.suppress")) {
            NexusLogger.error("    Type: NPFException", NexusLogger.LogType.COMMON);
            NexusLogger.error("    Module: %s", NexusLogger.LogType.COMMON, type.name());
        }
    }

}
