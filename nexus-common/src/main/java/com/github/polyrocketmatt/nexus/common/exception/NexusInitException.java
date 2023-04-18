package com.github.polyrocketmatt.nexus.common.exception;

import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

public class NexusInitException extends NexusException {

    public NexusInitException(String message) {
        super(message);

        NexusLogger.error("    Type: NexusInitException", NexusLogger.LogType.COMMON);
    }
}
