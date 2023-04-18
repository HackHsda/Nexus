package com.github.polyrocketmatt.nexus.common.exception;

import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

public class NexusException extends RuntimeException {

    public NexusException(String message) {
        super(message);

        StringBuilder traceBuilder = new StringBuilder();
        for (StackTraceElement element : getStackTrace()) {
            traceBuilder.append(element.toString());
            traceBuilder.append("\n");
        }

        NexusLogger.error("NexusException: %s", NexusLogger.LogType.COMMON, message);
        NexusLogger.error("    Stack Trace: %s", NexusLogger.LogType.COMMON, traceBuilder.toString());
    }

}
