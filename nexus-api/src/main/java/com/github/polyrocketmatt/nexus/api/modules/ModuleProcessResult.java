package com.github.polyrocketmatt.nexus.api.modules;

import com.github.polyrocketmatt.nexus.api.entity.NexusEntity;
import org.jetbrains.annotations.NotNull;

public abstract class ModuleProcessResult {

    protected final @NotNull NexusEntity entity;
    protected final boolean failed;

    public ModuleProcessResult(@NotNull NexusEntity entity, boolean failed) {
        this.entity = entity;
        this.failed = failed;
    }

    public @NotNull NexusEntity getEntity() {
        return entity;
    }

    public boolean isFailed() {
        return failed;
    }
}
