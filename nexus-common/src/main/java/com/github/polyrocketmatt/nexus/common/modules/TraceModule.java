package com.github.polyrocketmatt.nexus.common.modules;

import com.github.polyrocketmatt.nexus.api.modules.NexusModule;
import com.github.polyrocketmatt.nexus.api.modules.NexusModuleType;
import org.jetbrains.annotations.NotNull;

public class TraceModule extends NexusModule {

    @Override
    public @NotNull NexusModuleType getModuleType() {
        return NexusModuleType.PACKET_TRACER;
    }
}
