package com.github.polyrocketmatt.nexus.common.exception;

import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

import java.util.Arrays;

public class NexusThreadingException extends NexusException {

    public NexusThreadingException(String message, String... data) {
        super(message);

        if (!Nexus.getPlatform().getConfiguration().getBoolean("logging.suppress")) {
            Arrays.stream(data).forEach(str -> NexusLogger.error("    Error: %s", NexusLogger.LogType.COMMON, str));
        }
    }
}
