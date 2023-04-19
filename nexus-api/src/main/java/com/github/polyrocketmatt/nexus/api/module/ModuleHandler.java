package com.github.polyrocketmatt.nexus.api.module;

import com.github.polyrocketmatt.nexus.api.PlatformType;
import com.github.polyrocketmatt.nexus.api.entity.NexusEntity;
import com.github.polyrocketmatt.nexus.api.events.NexusEvent;

public interface ModuleHandler {

    default PlatformType getPlatformType() {
        return PlatformType.UNKNOWN;
    }

    void process(NexusEvent event, NexusEntity player);

}
