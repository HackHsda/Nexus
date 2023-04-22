package com.github.polyrocketmatt.nexus.common.modules;

import com.github.polyrocketmatt.nexus.api.entity.NexusEntity;
import com.github.polyrocketmatt.nexus.api.events.NexusEvent;
import com.github.polyrocketmatt.nexus.api.modules.ModuleProcessResult;
import com.github.polyrocketmatt.nexus.api.modules.NexusModule;
import com.github.polyrocketmatt.nexus.api.modules.NexusModuleType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TraceModule extends NexusModule {

    @Override
    public @NotNull Set<Class<?>> getObservedEvents() {
        return Set.of();
    }

    @Override
    public @NotNull NexusModuleType getModuleType() {
        return NexusModuleType.PACKET_TRACER;
    }

    @Override
    public @NotNull ModuleProcessResult process(@NotNull NexusEntity nexusEntity, @NotNull NexusEvent nexusEvent) {
        return null;
    }
}
