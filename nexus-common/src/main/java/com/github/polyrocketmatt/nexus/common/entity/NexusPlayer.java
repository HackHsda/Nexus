package com.github.polyrocketmatt.nexus.common.entity;

import com.github.polyrocketmatt.nexus.api.entity.NexusCommunicativeEntity;
import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.client.NexusPlayerStatusClient;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class NexusPlayer implements NexusCommunicativeEntity {

    protected final UUID uuid;
    protected final String ipAddress;
    protected NexusPlayerData playerData;

    public NexusPlayer(UUID uuid, String ipAddress) {
        this.uuid = uuid;
        this.ipAddress = ipAddress;

        //  Schedule a task to check the latest player data from the database (or create a new one if it doesn't exist)
        Nexus.getThreadManager().submit(() -> {
            //  Get available client and perform GET
            var client = new NexusPlayerStatusClient(this);
            playerData = client.get(NexusPlayerData.class);

            NexusLogger.inform("Player data for %s has been loaded", NexusLogger.LogType.COMMON, uuid.toString());
            NexusLogger.inform("    UUID: %s", NexusLogger.LogType.COMMON, uuid.toString());
            NexusLogger.inform("    Connections: %s", NexusLogger.LogType.COMMON, playerData.connections().size());
        });
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public String getIpAddress() {
        return ipAddress    ;
    }

    public NexusPlayerData getPlayerData() {
        return playerData;
    }

    public abstract boolean hasPermission(@NotNull String permission);

}
