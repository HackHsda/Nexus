package com.github.polyrocketmatt.nexus.common.manager;

import com.github.polyrocketmatt.nexus.api.entity.NexusPlayer;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerManager {

    private final Set<NexusPlayer> players;

    public PlayerManager() {
        this.players = new HashSet<>();
    }

    public void registerPlayer(@NotNull NexusPlayer player) {
        this.players.add(player);

        NexusLogger.inform("Registered player: %s".formatted(player.getUniqueId()), NexusLogger.LogType.COMMON);
    }

    public void unregisterPlayer(@NotNull UUID uuid) {
        this.players.removeIf(player -> player.getUniqueId().equals(uuid));

        NexusLogger.inform("Unregistered player: %s".formatted(uuid), NexusLogger.LogType.COMMON);
    }

    public @Nullable NexusPlayer getPlayer(@NotNull UUID uuid) {
        return this.players.stream().filter(player -> player.getUniqueId().equals(uuid)).findFirst().orElse(null);
    }

}
