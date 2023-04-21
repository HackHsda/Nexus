package com.github.polyrocketmatt.nexus.common.manager;

import com.github.polyrocketmatt.nexus.api.manager.NexusManager;
import com.github.polyrocketmatt.nexus.common.entity.NexusPlayer;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerManager implements NexusManager {

    private final Set<NexusPlayer> players;

    public PlayerManager() {
        this.players = new HashSet<>();

        NexusLogger.inform("Initialised %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
    }

    @Override
    public void close() {
        //  TODO: Perform Database Update!
        players.clear();

        NexusLogger.inform("Closed %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
    }

    @Override
    public void log() {
        NexusLogger.inform("Player Manager", NexusLogger.LogType.COMMON);
        NexusLogger.inform("--------------", NexusLogger.LogType.COMMON);
        NexusLogger.inform("    Players: %s", NexusLogger.LogType.COMMON, players.size());
        NexusLogger.inform("", NexusLogger.LogType.COMMON);
    }

    public void registerPlayer(@NotNull NexusPlayer player) {
        if (players.contains(player))
            return;
        players.add(player);

        NexusLogger.inform("Registered player: %s".formatted(player.getUniqueId()), NexusLogger.LogType.COMMON);
    }

    public void unregisterPlayer(@NotNull UUID uuid) {
        NexusPlayer player = getPlayer(uuid);
        if (player != null)
            players.remove(player);

        NexusLogger.inform("Unregistered player: %s".formatted(uuid), NexusLogger.LogType.COMMON);
    }

    public @Nullable NexusPlayer getPlayer(@NotNull UUID uuid) {
        return players.stream().filter(player -> player.getUniqueId().equals(uuid)).findFirst().orElse(null);
    }

}
