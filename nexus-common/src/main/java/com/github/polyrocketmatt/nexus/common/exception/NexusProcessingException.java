package com.github.polyrocketmatt.nexus.common.exception;

import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

public class NexusProcessingException extends NexusException {

    public NexusProcessingException(String message, Exception nested) {
        super(message);

        StringBuilder traceBuilder = new StringBuilder();
        for (StackTraceElement element : nested.getStackTrace()) {
            traceBuilder.append(element.toString());
            traceBuilder.append("\n");
        }

        NexusLogger.error("    Nested Exception: %s", NexusLogger.LogType.COMMON, nested.getMessage());
        NexusLogger.error("    Nested Stack Trace: %s", NexusLogger.LogType.COMMON, traceBuilder.toString());
    }

}
