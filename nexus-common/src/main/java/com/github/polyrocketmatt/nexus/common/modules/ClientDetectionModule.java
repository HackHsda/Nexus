package com.github.polyrocketmatt.nexus.common.modules;

import com.github.polyrocketmatt.nexus.api.module.NexusModule;
import com.github.polyrocketmatt.nexus.api.module.NexusModuleType;
import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.client.NexusModClient;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import com.github.polyrocketmatt.nexus.common.utils.Pair;
import com.github.polyrocketmatt.nexus.common.utils.processor.StringProcessor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class ClientDetectionModule extends NexusModule {

    private static final String CONNECTION_KEY_ROUTE = "modules.client-detection.keys";
    private static final String CONNECTION_KEY_PROP_ROUTE = "client-detection.key";
    private final Map<String, Pair<String, String>> connectionKeys;
    private final Map<String, Integer> mods;

    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    public ClientDetectionModule() {
        this.connectionKeys = new HashMap<>();
        this.mods = new HashMap<>();

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

        ExecutorService service = Nexus.getThreadManager().getService(1);

        service.submit(() -> {
            //  Check if mods.nexus exists
            File file = new File(Nexus.getPlatform().getDataDirectory(), "mods.nexus");
            if (!file.exists()) {
                System.out.println("Creating mods.nexus file");

                try {
                    file.createNewFile();
                } catch (Exception ex) {
                    NexusLogger.warn("Unable to create mods.nexus file", NexusLogger.LogType.COMMON);
                }

                fetchAndPersistMods(file);
            } else {
                NexusLogger.inform("Loading Forge mods from mods.nexus", NexusLogger.LogType.COMMON);

                //  Load forge mods from file
                try {
                    var reader = new Scanner(file);
                    while (reader.hasNextLine()) {
                        String line = reader.nextLine();
                        if (line.isEmpty() || line.isBlank())
                            continue;

                        String[] split = line.split(":");
                        if (split.length == 2) {
                            String mod = split[0];
                            int index = Integer.parseInt(split[1]);

                            mods.put(mod, index);
                        }
                    }
                } catch (Exception ex) {
                    NexusLogger.warn("Unable to load Forge mods from mods.nexus", NexusLogger.LogType.COMMON);
                }
            }
        });

        Nexus.getThreadManager().handleTermination(service, 25000);
        NexusLogger.inform("Loaded %s connection keys", NexusLogger.LogType.COMMON, connectionKeys.size());
        NexusLogger.inform("Loaded %s mods", NexusLogger.LogType.COMMON, mods.size());
    }

    @SuppressWarnings("unchecked")
    private void fetchAndPersistMods(File file) {
        //  Load Forge mods
        Nexus.getThreadManager().accept(
                () -> {
                    //  Get available client and perform GET
                    var client = new NexusModClient();
                    return (Map<String, String>) client.get(Map.class);
                },
                data -> {
                    for (Map.Entry<String, String> entry : data.entrySet()) {
                        try {
                            int index = Integer.parseInt(entry.getKey());
                            mods.put(entry.getValue(), index);
                        } catch (NumberFormatException ex) {
                            NexusLogger.warn("Unable to parse Forge mod ID: %s", NexusLogger.LogType.COMMON, entry.getKey());
                        }
                    }

                    NexusLogger.inform("Most popular mod data has been loaded (Source: CurseForge)", NexusLogger.LogType.COMMON);
                    NexusLogger.inform("    Mods: %s", NexusLogger.LogType.COMMON, data.size());

                    mods.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(entry ->
                            NexusLogger.inform("     - %s: %s", NexusLogger.LogType.COMMON, entry.getValue(), entry.getKey()));
                },
                25000
        );

        System.out.println("Mods: " + mods.size());
        persistForgeMods(file);
    }

    private void persistForgeMods(File file) {
        try {
            System.out.println("Writing mods to file");

            FileWriter writer = new FileWriter(file);

            //  Write all mods to file
            for (Map.Entry<String, Integer> entry : mods.entrySet())
                writer.write("%s:%s\n".formatted(entry.getKey(), entry.getValue()));
            writer.close();
        } catch (Exception ex) {
            NexusLogger.warn("Unable to persist Forge mods in mods.nexus", NexusLogger.LogType.COMMON);
        }
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
        return StringProcessor.getProcessor().contains(message, mods.keySet(), ':', 10000);
    }

    @Override
    public @NotNull NexusModuleType getModuleType() {
        return NexusModuleType.CLIENT_DETECTION;
    }

}
