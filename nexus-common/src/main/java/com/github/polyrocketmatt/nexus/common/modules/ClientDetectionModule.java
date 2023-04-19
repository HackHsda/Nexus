package com.github.polyrocketmatt.nexus.common.modules;

import com.github.polyrocketmatt.nexus.api.module.NexusModule;
import com.github.polyrocketmatt.nexus.api.module.NexusModuleType;
import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.client.NexusForgeClient;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import com.github.polyrocketmatt.nexus.common.utils.Pair;
import com.github.polyrocketmatt.nexus.common.utils.processor.StringProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDetectionModule implements NexusModule {

    private static final String CONNECTION_KEY_ROUTE = "modules.client-detection.keys";
    private static final String CONNECTION_KEY_PROP_ROUTE = "client-detection.key";
    private final Map<String, Pair<String, String>> connectionKeys;
    private final Map<String, Integer> forgeMods;

    @SuppressWarnings("unchecked")
    public ClientDetectionModule() {
        this.connectionKeys = new HashMap<>();
        this.forgeMods = new HashMap<>();

        //  Get all the connection keys from the configuration
        Set<String> keys = Nexus.getPlatform().getConfiguration().getSection(CONNECTION_KEY_ROUTE).getKeys()
                .stream().map(key -> (String) key).collect(Collectors.toSet());

        for (String key : keys) {
            boolean isEnabled = Nexus.getPlatform().getConfiguration().getBoolean("%s.%s".formatted(CONNECTION_KEY_ROUTE, key));
            if (isEnabled) {
                String channel = Nexus.getProperties().getProperty("%s.%s.channel".formatted(CONNECTION_KEY_PROP_ROUTE, key));
                String message = Nexus.getProperties().getProperty("%s.%s.message".formatted(CONNECTION_KEY_PROP_ROUTE, key));

                connectionKeys.put(key, new Pair<>(channel, message));
            }
        }

        //  Load Forge mods
        Nexus.getThreadManager().accept(
                () -> {
                    //  Get available client and perform GET
                    var client = new NexusForgeClient();
                    return (Map<String, String>) client.get(Map.class);
                },
                data -> {
                    for (Map.Entry<String, String> entry : data.entrySet()) {
                        try {
                            int index = Integer.parseInt(entry.getKey());
                            forgeMods.put(entry.getValue(), index);
                        } catch (NumberFormatException ex) {
                            NexusLogger.warn("Unable to parse Forge mod ID: %s", NexusLogger.LogType.COMMON, entry.getKey());
                        }
                    }

                    NexusLogger.inform("Mod data has been loaded", NexusLogger.LogType.COMMON);
                    NexusLogger.inform("    Mods: %s", NexusLogger.LogType.COMMON, data.size());

                    forgeMods.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(entry ->
                            NexusLogger.inform("     - %s: %s", NexusLogger.LogType.COMMON, entry.getValue(), entry.getKey()));
                }
        );
    }

    public @NotNull Pair<Boolean, String> verify(String channel, String message) {
        for (Map.Entry<String, Pair<String, String>> entry : connectionKeys.entrySet()) {
            String channelCheck = entry.getValue().getFirst();
            String messageCheck = entry.getValue().getSecond();

            if (channel.contains(channelCheck) && message.contains(messageCheck))
                return new Pair<>(false, entry.getKey());
        }

        return new Pair<>(true, "");
    }

    public @NotNull Set<String> parseForgeMods(String message) {
        return StringProcessor.getProcessor().contains(message, forgeMods.keySet(), ':');
    }

    public @NotNull Set<String> parseFabricMods(String message) {
        return Set.of();
    }

    @Override
    public @NotNull NexusModuleType getModuleType() {
        return NexusModuleType.CLIENT_DETECTION;
    }
}
