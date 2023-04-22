package com.github.polyrocketmatt.nexus.common.modules.result;

import com.github.polyrocketmatt.nexus.api.modules.ModuleProcessResult;
import com.github.polyrocketmatt.nexus.common.entity.NexusPlayer;
import org.jetbrains.annotations.NotNull;

public class ClientDetectionResult extends ModuleProcessResult {

    private final @NotNull String clientName;

    public ClientDetectionResult(@NotNull NexusPlayer player, boolean failed, @NotNull String clientName) {
        super(player, failed);
        this.clientName = clientName;
    }

    @Override
    public @NotNull NexusPlayer getEntity() {
        return (NexusPlayer) entity;
    }

    public @NotNull String getClientName() {
        return clientName;
    }
}
