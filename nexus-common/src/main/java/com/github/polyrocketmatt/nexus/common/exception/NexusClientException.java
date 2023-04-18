package com.github.polyrocketmatt.nexus.common.exception;

import com.github.polyrocketmatt.nexus.common.client.NexusClient;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

public class NexusClientException extends NexusException {

    public NexusClientException(String message, String uri, NexusClient.Method method) {
        super(message);

        NexusLogger.error("    URI: ", NexusLogger.LogType.COMMON, uri);
        NexusLogger.error("    Method: ", NexusLogger.LogType.COMMON, method.name());
    }
}
