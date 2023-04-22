package com.github.polyrocketmatt.nexus.common.modules.result;

import com.github.polyrocketmatt.nexus.api.modules.ModuleProcessResult;
import com.github.polyrocketmatt.nexus.common.entity.NexusPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModDetectionResult extends ModuleProcessResult {

    private final @NotNull Set<String> mods;

    public ModDetectionResult(@NotNull NexusPlayer player, boolean failed, @NotNull Set<String> mods) {
        super(player, failed);
        this.mods = mods;
    }

    @Override
    public @NotNull NexusPlayer getEntity() {
        return (NexusPlayer) entity;
    }

    public @NotNull Set<String> getMods() {
        return mods;
    }
}