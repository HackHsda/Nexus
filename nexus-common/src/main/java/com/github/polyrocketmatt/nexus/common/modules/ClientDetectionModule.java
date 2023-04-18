package com.github.polyrocketmatt.nexus.common.modules;

import com.github.polyrocketmatt.nexus.api.module.NexusModule;
import com.github.polyrocketmatt.nexus.api.module.NexusModuleType;
import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDetectionModule implements NexusModule {

    private static final String CONNECTION_KEY_ROUTE = "modules.client-detection.keys";
    private static final String CONNECTION_KEY_PROP_ROUTE = "client-detection.key";
    private final Map<String, Pair<String, String>> connectionKeys;

    public ClientDetectionModule() {
        this.connectionKeys = new HashMap<>();

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
    }

    public Pair<Boolean, String> verify(String channel, String message) {
        for (Map.Entry<String, Pair<String, String>> entry : connectionKeys.entrySet()) {
            String channelCheck = entry.getValue().getFirst();
            String messageCheck = entry.getValue().getSecond();

            if (channel.contains(channelCheck) && message.contains(messageCheck))
                return new Pair<>(false, entry.getKey());
        }
        return new Pair<>(true, "");
    }

    @Override
    public @NotNull NexusModuleType getModuleType() {
        return NexusModuleType.CLIENT_DETECTION;
    }
}
