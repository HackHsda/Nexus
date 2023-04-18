package com.github.polyrocketmatt.nexus.api;

import com.github.polyrocketmatt.nexus.api.entity.NexusEntity;
import com.github.polyrocketmatt.nexus.api.events.NexusListener;
import com.github.polyrocketmatt.nexus.api.metrics.NexusMetrics;
import com.github.polyrocketmatt.nexus.api.module.NexusModuleType;
import com.github.polyrocketmatt.nexus.api.module.NexusModule;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface NexusPlatform {

    @NotNull String getPrefix();

    @NotNull YamlDocument getConfiguration();

    void registerListener(@NotNull NexusListener listener);

    void registerModule(@NotNull NexusModule module);

    @NotNull <T extends NexusModule> T getModule(@NotNull NexusModuleType type);

    @NotNull NexusEntity getPlayer(@NotNull UUID uuid);

    void registerPlayer(@NotNull UUID uuid);

    void unregisterPlayer(@NotNull UUID uuid);

    boolean getEnvBoolean(@NotNull String key);

    int getEnvInt(@NotNull String key);

    double getEnvDouble(@NotNull String key);

    String getEnvString(@NotNull String key);

    @NotNull NexusMetrics initMetrics();

    @NotNull PlatformType getPlatformType();

}
