package com.github.polyrocketmatt.nexus.common.manager;

import com.github.polyrocketmatt.nexus.api.events.NexusEvent;
import com.github.polyrocketmatt.nexus.api.manager.NexusManager;
import com.github.polyrocketmatt.nexus.common.entity.NexusPlayer;
import com.github.polyrocketmatt.nexus.common.exception.NexusEntityException;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

public class PlayerManager implements NexusManager {

    private final Set<NexusPlayer> players;
    private final Map<UUID, Queue<NexusEvent>> messageBox;

    public PlayerManager() {
        this.players = new HashSet<>();
        this.messageBox = new HashMap<>();

        NexusLogger.inform("Initialised %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
    }

    @Override
    public void close() {
        //  TODO: Perform Database Update!
        this.players.clear();

        NexusLogger.inform("Closed %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
    }

    public void registerPlayer(@NotNull NexusPlayer player) {
        if (this.players.contains(player))
            return;
        this.players.add(player);
        this.messageBox.put(player.getUniqueId(), new LinkedList<>());

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
