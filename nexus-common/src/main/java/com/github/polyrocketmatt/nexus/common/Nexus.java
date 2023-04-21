package com.github.polyrocketmatt.nexus.common;

import com.github.polyrocketmatt.nexus.api.NexusPlatform;
import com.github.polyrocketmatt.nexus.api.manager.NexusManager;
import com.github.polyrocketmatt.nexus.common.exception.NexusInitException;
import com.github.polyrocketmatt.nexus.common.manager.CommandManager;
import com.github.polyrocketmatt.nexus.common.manager.EventManager;
import com.github.polyrocketmatt.nexus.common.manager.MetricsManager;
import com.github.polyrocketmatt.nexus.common.manager.ModuleManager;
import com.github.polyrocketmatt.nexus.common.manager.PlayerManager;
import com.github.polyrocketmatt.nexus.common.manager.ThreadManager;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import com.github.polyrocketmatt.nexus.common.utils.ResourceLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class Nexus {

    private final static Nexus INSTANCE = new Nexus();

    protected NexusPlatform platform;
    protected File workingDirectory;
    private Properties properties;
    private Set<NexusManager> managers;
    private EventManager eventManager;
    private MetricsManager metricsManager;
    private ModuleManager moduleManager;
    private PlayerManager playerManager;
    private ThreadManager threadManager;
    private CommandManager commandManager;

    private Nexus() {
        this.platform = null;
        this.workingDirectory = null;
        this.properties = null;
        this.managers = new HashSet<>();
    }

    public static void loadNexus(NexusPlatform platform, File workingDirectory) {
        INSTANCE.platform = platform;
        INSTANCE.workingDirectory = workingDirectory;
        INSTANCE.properties = new Properties();

        //  Initialise logger and properties
        INSTANCE.loadLogger();
        INSTANCE.loadProperties();

        //  Load managers
        INSTANCE.eventManager = new EventManager();
        INSTANCE.moduleManager = new ModuleManager();
        INSTANCE.playerManager = new PlayerManager();
        INSTANCE.threadManager = new ThreadManager();
        INSTANCE.commandManager = new CommandManager();

        //  Add all managers
        INSTANCE.managers.add(INSTANCE.eventManager);
        INSTANCE.managers.add(INSTANCE.moduleManager);
        INSTANCE.managers.add(INSTANCE.playerManager);
        INSTANCE.managers.add(INSTANCE.threadManager);
        INSTANCE.managers.add(INSTANCE.commandManager);

        NexusLogger.inform("Nexus loaded successfully for platform: %s", NexusLogger.LogType.COMMON, platform.getPlatformType().name());
    }

    private void loadLogger() {
        String prefixFormat = "dd'_'M'_'yyyy'_'hh'_'mm'_'ss";
        SimpleDateFormat format = new SimpleDateFormat(prefixFormat);
        String date = format.format(new Date());
        NexusLogger.initialise(new File(workingDirectory, "nexus_%s.log".formatted(date)), "yyyy-MM-dd HH:mm:ss");
        NexusLogger.inform("Loading Nexus...", NexusLogger.LogType.COMMON);
    }

    private void loadProperties() {
        try {
            properties.load(ResourceLoader.getResource("nexus.properties"));
        } catch (IOException ex) {
            NexusLogger.error("Failed to load nexus.properties", ex);
        }
    }

    public static void unload() {
        NexusLogger.inform("Unloading Nexus...", NexusLogger.LogType.COMMON);

        //  Close all managers
        INSTANCE.managers.forEach(NexusManager::close);
        INSTANCE.managers.clear();

        NexusLogger.inform("Nexus unloaded successfully", NexusLogger.LogType.COMMON);
    }

    private static <T> T checkInitialised(T object) throws NexusInitException {
        if (object == null)
            throw new NexusInitException("Nexus has not been initialised yet!");
        return object;
    }

    public static void registerManager(@NotNull NexusManager manager) {
        INSTANCE.managers.add(manager);
    }

    public static @NotNull NexusPlatform getPlatform() throws NexusInitException {
        return checkInitialised(INSTANCE.platform);
    }

    public static @NotNull Properties getProperties() throws NexusInitException {
        return checkInitialised(INSTANCE.properties);
    }

    public static @NotNull EventManager getEventManager() {
        return checkInitialised(INSTANCE.eventManager);
    }

    public static @NotNull ModuleManager getModuleManager() {
        return checkInitialised(INSTANCE.moduleManager);
    }

    public static @NotNull PlayerManager getPlayerManager() {
        return checkInitialised(INSTANCE.playerManager);
    }

    public static @NotNull ThreadManager getThreadManager() {
        return checkInitialised(INSTANCE.threadManager);
    }

    public static @NotNull CommandManager getCommandManager() {
        return checkInitialised(INSTANCE.commandManager);
    }

}
