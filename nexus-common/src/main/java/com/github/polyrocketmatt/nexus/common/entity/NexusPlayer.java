package com.github.polyrocketmatt.nexus.common.entity;

import com.github.polyrocketmatt.nexus.api.entity.NexusEntity;
import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.client.NexusPlayerStatusClient;
import com.github.polyrocketmatt.nexus.common.exception.NexusModuleException;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class NexusPlayer implements NexusEntity {

    protected final UUID uuid;
    protected NexusPlayerData playerData;

    public NexusPlayer(UUID uuid) {
        this.uuid = uuid;

        //  Schedule a task to check the latest player data from the database (or create a new one if it doesn't exist)
        Nexus.getThreadManager().submit(() -> {
            //  Get available client and perform GET
            var client = new NexusPlayerStatusClient(this);
            NexusPlayerData data = client.get(NexusPlayerData.class);

            NexusLogger.inform("Player data for %s has been loaded", NexusLogger.LogType.COMMON, uuid.toString());
            NexusLogger.inform("    UUID: %s", NexusLogger.LogType.COMMON, uuid.toString());
            NexusLogger.inform("    Data: %s", NexusLogger.LogType.COMMON, data.data());
        });

        //  It is possible we already received messages from the client before the data was loaded
        Nexus.getThreadManager().submit(() -> {
            var event = Nexus.getEventManager().deque(uuid);
            while (event != null) {
                //  Get the appropriate module that handles this event
                var module = Nexus.getModuleManager().getModule(event.getModuleHandle());
                if (module == null)
                    throw new NexusModuleException("Module %s does not exist", event.getModuleHandle());

                module.getModuleHandler(Nexus.getPlatform().getPlatformType());
                event = Nexus.getEventManager().deque(uuid);
            }
        });
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public NexusPlayerData getPlayerData() {
        return playerData;
    }

    public abstract void sendMessage(@NotNull String message);
}
