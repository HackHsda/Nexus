package com.github.polyrocketmatt.nexus.api.entity;

import org.jetbrains.annotations.NotNull;

public interface NexusCommunicativeEntity extends NexusEntity {

    void sendMessage(@NotNull String message);

    boolean hasPermission(@NotNull String permission);

}
