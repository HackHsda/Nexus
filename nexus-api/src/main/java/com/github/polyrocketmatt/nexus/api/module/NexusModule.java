package com.github.polyrocketmatt.nexus.api.module;

import com.github.polyrocketmatt.nexus.api.PlatformType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public abstract class NexusModule {

    private final Set<ModuleProcessor> handlers;

    public NexusModule() {
        this.handlers = new HashSet<>();
    }

    public abstract @NotNull NexusModuleType getModuleType();

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
