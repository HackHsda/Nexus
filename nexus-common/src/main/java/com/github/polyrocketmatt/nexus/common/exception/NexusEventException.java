package com.github.polyrocketmatt.nexus.common.exception;

import com.github.polyrocketmatt.nexus.api.events.ExternalEventListener;
import com.github.polyrocketmatt.nexus.api.events.InternalEventListener;
import com.github.polyrocketmatt.nexus.api.events.NexusEvent;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

public class NexusEventException extends NexusException {

    public NexusEventException(String message) {
        super(message);
    }

    public NexusEventException(String message, NexusEvent event) {
        super(message);

        NexusLogger.error("    Event: %s", NexusLogger.LogType.COMMON, event.getClass().getSimpleName());
        NexusLogger.error("    Module Handle: %s", NexusLogger.LogType.COMMON, event.getModuleHandle());
        NexusLogger.error("    UUID: %s", NexusLogger.LogType.COMMON, event.getUniqueId());
    }

    public NexusEventException(String message, ExternalEventListener listener) {
        super(message);

        NexusLogger.error("    Listener (EXT): %s", NexusLogger.LogType.COMMON, listener.getClass().getSimpleName());
    }

    public NexusEventException(String message, InternalEventListener listener) {
        super(message);

        NexusLogger.error("    Listener (INT): %s", NexusLogger.LogType.COMMON, listener.getClass().getSimpleName());
    }

}
