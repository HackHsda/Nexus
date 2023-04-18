package com.github.polyrocketmatt.nexus.common.entity;

import com.github.polyrocketmatt.nexus.api.entity.NexusEntity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class NexusPlayer implements NexusEntity {

    protected final UUID uuid;

    public NexusPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public abstract void sendMessage(@NotNull String message);

    public abstract NexusPlayerData getPlayerData();
}
