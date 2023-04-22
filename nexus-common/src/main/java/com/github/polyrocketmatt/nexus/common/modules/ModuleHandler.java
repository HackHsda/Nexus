package com.github.polyrocketmatt.nexus.common.modules;

import com.github.polyrocketmatt.nexus.api.modules.ModuleProcessor;
import com.github.polyrocketmatt.nexus.api.modules.NexusModuleType;
import com.github.polyrocketmatt.nexus.common.Nexus;

public abstract class ModuleHandler implements ModuleProcessor {

    public ModuleHandler(NexusModuleType type) {
        Nexus.getModuleManager().registerModuleHandler(type, this);
    }

}
