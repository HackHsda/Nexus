package com.github.polyrocketmatt.nexus.api.modules;

import com.github.polyrocketmatt.nexus.api.PlatformType;
import com.github.polyrocketmatt.nexus.api.entity.NexusEntity;
import com.github.polyrocketmatt.nexus.api.events.NexusEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public abstract class NexusModule {

    private final Set<ModuleProcessor> handlers;

    public NexusModule() {
        this.handlers = new HashSet<>();
    }

    public abstract @NotNull Set<Class<?>> getObservedEvents();

    public abstract @NotNull NexusModuleType getModuleType();

    public abstract @NotNull ModuleProcessResult process(@NotNull NexusEntity nexusEntity, @NotNull NexusEvent nexusEvent);

    public @Nullable ModuleProcessor getModuleHandler(PlatformType type) {
        for (ModuleProcessor handler : handlers)
            if (handler.getPlatformType() == type)
                return handler;

        return null;
    }

    public void addModuleHandler(ModuleProcessor handler) {
        this.handlers.add(handler);
    }

}
