package com.github.polyrocketmatt.nexus.common.modules.result;

import com.github.polyrocketmatt.nexus.api.entity.NexusEntity;
import com.github.polyrocketmatt.nexus.api.modules.ModuleProcessResult;
import org.jetbrains.annotations.NotNull;

public class PlainProcessResult extends ModuleProcessResult {

    public PlainProcessResult(@NotNull NexusEntity entity, boolean failed) {
        super(entity, failed);
    }

}