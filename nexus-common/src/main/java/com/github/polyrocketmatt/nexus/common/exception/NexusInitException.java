package com.github.polyrocketmatt.nexus.common.exception;

import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

public class NexusInitException extends NexusException {

    public NexusInitException(String message) {
        super(message);

        if (!Nexus.getPlatform().getConfiguration().getBoolean("logging.suppress")) {
            NexusLogger.error("    Type: NexusInitException", NexusLogger.LogType.COMMON);
        }
    }
}
