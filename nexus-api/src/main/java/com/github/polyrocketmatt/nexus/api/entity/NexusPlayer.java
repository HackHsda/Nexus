package com.github.polyrocketmatt.nexus.api.entity;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface NexusPlayer {

    default @NotNull UUID getUniqueId() {
        return UUID.randomUUID();
    }

    void sendMessage(String message);

    String json();

}
