package com.github.polyrocketmatt.nexus.common.modules;

import com.github.polyrocketmatt.nexus.api.module.NexusModule;
import com.github.polyrocketmatt.nexus.api.module.NexusModuleType;
import org.jetbrains.annotations.NotNull;

public class ClientConnectionModule extends NexusModule {

    @Override
    public @NotNull NexusModuleType getModuleType() {
        return NexusModuleType.CLIENT_CONNECTION;
    }
}
