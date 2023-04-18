package com.github.polyrocketmatt.nexus.api.module;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NexusModule {

    @NotNull NexusModuleType getModuleType();

}
