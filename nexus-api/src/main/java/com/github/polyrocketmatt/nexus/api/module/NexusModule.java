package com.github.polyrocketmatt.nexus.api.module;

import com.github.polyrocketmatt.nexus.api.PlatformType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public abstract class NexusModule {

    private Set<ModuleHandler> handlers;

    public NexusModule() {
        this.handlers = new HashSet<>();
    }

    public abstract @NotNull NexusModuleType getModuleType();

    public @Nullable ModuleHandler getModuleHandler(PlatformType type) {
        for (ModuleHandler handler : handlers) {
            if (handler.getPlatformType() == type)
                return handler;
        }

        return null;
    }

    public void addModuleHandler(ModuleHandler handler) {
        this.handlers.add(handler);
    }

}
