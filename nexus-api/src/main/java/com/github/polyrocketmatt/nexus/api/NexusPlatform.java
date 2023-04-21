package com.github.polyrocketmatt.nexus.api;

import com.github.polyrocketmatt.nexus.api.entity.NexusEntity;
import com.github.polyrocketmatt.nexus.api.events.ExternalEventListener;
import com.github.polyrocketmatt.nexus.api.metrics.NexusMetrics;
import com.github.polyrocketmatt.nexus.api.module.ModuleProcessor;
import com.github.polyrocketmatt.nexus.api.module.NexusModuleType;
import com.github.polyrocketmatt.nexus.api.module.NexusModule;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

public interface NexusPlatform {

    @NotNull File getLoggingDirectory();

    @NotNull File getDataDirectory();

    @NotNull String getPrefix();

    @NotNull YamlDocument getConfiguration();

    @NotNull YamlDocument getMessages();

    void registerListener(@NotNull ExternalEventListener listener);

    void registerModule(@NotNull NexusModule module, @NotNull ModuleProcessor... processors);

    @NotNull <T extends NexusModule> T getModule(@NotNull NexusModuleType type);

    NexusEntity getPlayer(@NotNull UUID uuid);

    void registerPlayer(@NotNull UUID uuid);

    void unregisterPlayer(@NotNull UUID uuid);

    boolean getEnvBoolean(@NotNull String key);

    int getEnvInt(@NotNull String key);

    double getEnvDouble(@NotNull String key);

    String getEnvString(@NotNull String key);

    @NotNull NexusMetrics initMetrics();

    default @NotNull PlatformType getPlatformType() {
        return PlatformType.UNKNOWN;
    }

}
