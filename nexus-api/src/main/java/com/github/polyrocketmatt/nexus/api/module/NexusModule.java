package com.github.polyrocketmatt.nexus.api.module;

import com.github.polyrocketmatt.nexus.api.PlatformType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NexusModule {

    @NotNull NexusModuleType getModuleType();

    @Nullable ModuleHandler getModuleHandler(PlatformType type);

}
