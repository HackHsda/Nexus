package com.github.polyrocketmatt.nexus.api.modules;

import com.github.polyrocketmatt.nexus.api.PlatformType;

public interface ModuleProcessor {

    default PlatformType getPlatformType() { return PlatformType.UNKNOWN; }

    void handle(ModuleProcessResult processResult);

}
