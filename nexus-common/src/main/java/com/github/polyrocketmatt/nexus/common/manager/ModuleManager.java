package com.github.polyrocketmatt.nexus.common.manager;

import com.github.polyrocketmatt.nexus.api.manager.NexusManager;
import com.github.polyrocketmatt.nexus.api.module.NexusModule;
import com.github.polyrocketmatt.nexus.api.module.NexusModuleType;
import com.github.polyrocketmatt.nexus.common.exception.NexusModuleException;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class ModuleManager implements NexusManager {

    private final Set<NexusModule> modules;

    public ModuleManager() {
        this.modules = new HashSet<>();
    }

    @Override
    public void close() {
        this.modules.clear();

        NexusLogger.inform("Closed %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
    }

    public void registerModule(NexusModule module) {
        if (isEnabled(module.getModuleType()))
            throw new NexusModuleException("Module %s is already enabled".formatted(module.getModuleType().name()), module.getModuleType());
        this.modules.add(module);

        NexusLogger.inform("Registered module: %s".formatted(module.getModuleType().name()), NexusLogger.LogType.COMMON);
    }

    public void unregisterModule(NexusModule module) {
        if (!isEnabled(module.getModuleType()))
            throw new NexusModuleException("Module %s is not enabled".formatted(module.getModuleType().name()), module.getModuleType());
        this.modules.remove(module);

        NexusLogger.inform("Unregistered module: %s".formatted(module.getModuleType().name()), NexusLogger.LogType.COMMON);
    }

    public boolean isEnabled(NexusModuleType type) {
        return this.modules.stream().anyMatch(module -> module.getModuleType() == type);
    }

    public @Nullable NexusModule getModule(NexusModuleType type) {
        return this.modules.stream().filter(module -> module.getModuleType() == type).findFirst().orElse(null);
    }

}
