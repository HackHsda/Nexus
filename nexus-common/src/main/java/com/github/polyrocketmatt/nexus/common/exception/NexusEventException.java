package com.github.polyrocketmatt.nexus.common.exception;

import com.github.polyrocketmatt.nexus.api.events.ExternalEventListener;
import com.github.polyrocketmatt.nexus.api.events.NexusEvent;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

public class NexusEventException extends NexusException {

    public NexusEventException(String message) {
        super(message);
    }

    public NexusEventException(String message, NexusEvent event) {
        super(message);
        boolean moduleIsNull = event.getModuleHandle() == null;
        boolean uuidIsNull = event.getUniqueId() == null;

        NexusLogger.error("    Event: %s", NexusLogger.LogType.COMMON, event.getClass().getSimpleName());
        NexusLogger.error("    Module Handle: %s", NexusLogger.LogType.COMMON, moduleIsNull ? "null" : event.getModuleHandle());
        NexusLogger.error("    UUID: %s", NexusLogger.LogType.COMMON, uuidIsNull ? "null" : event.getUniqueId());
    }

    public NexusEventException(String message, ExternalEventListener listener) {
        super(message);

        NexusLogger.error("    Listener (EXT): %s", NexusLogger.LogType.COMMON, listener.getClass().getSimpleName());
    }

}
