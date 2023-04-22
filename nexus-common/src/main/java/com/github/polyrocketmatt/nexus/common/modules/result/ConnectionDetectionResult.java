package com.github.polyrocketmatt.nexus.common.modules.result;

import com.github.polyrocketmatt.nexus.api.modules.ModuleProcessResult;
import com.github.polyrocketmatt.nexus.common.entity.NexusPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ConnectionDetectionResult extends ModuleProcessResult {

    public enum ConnectionType {
        TOR,
        PROXY,
        DATACENTER,
        ANONYMOUS,
        ATTACK,
        ABUSER,
        THREAT
    }

    private final @NotNull Set<ConnectionType> connectionTypes;

    public ConnectionDetectionResult(@NotNull NexusPlayer player, boolean failed, @NotNull Set<ConnectionType> connectionTypes) {
        super(player, failed);
        this.connectionTypes = connectionTypes;
    }

    @Override
    public @NotNull NexusPlayer getEntity() {
        return (NexusPlayer) entity;
    }

    @NotNull
    public Set<ConnectionType> getConnectionTypes() {
        return connectionTypes;
    }
}
