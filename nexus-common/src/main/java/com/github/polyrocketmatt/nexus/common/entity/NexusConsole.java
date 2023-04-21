package com.github.polyrocketmatt.nexus.common.entity;

import com.github.polyrocketmatt.nexus.api.entity.NexusCommunicativeEntity;
import org.jetbrains.annotations.NotNull;

public class NexusConsole implements NexusCommunicativeEntity {

    @Override
    public void sendMessage(@NotNull String message) {
        System.out.println(message);
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return true;
    }

}
