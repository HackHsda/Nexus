package com.github.polyrocketmatt.nexus.common;

import com.github.polyrocketmatt.nexus.api.NexusPlatform;
import com.github.polyrocketmatt.nexus.common.client.NexusClient;
import com.github.polyrocketmatt.nexus.common.exception.NexusInitException;
import com.github.polyrocketmatt.nexus.common.manager.EventManager;
import com.github.polyrocketmatt.nexus.common.manager.MetricsManager;
import com.github.polyrocketmatt.nexus.common.manager.ModuleManager;
import com.github.polyrocketmatt.nexus.common.manager.PlayerManager;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import com.github.polyrocketmatt.nexus.common.utils.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Nexus {

    private final static Nexus INSTANCE = new Nexus();

    protected NexusPlatform platform;
    protected File workingDirectory;
    private Properties properties;
    private EventManager eventManager;
    private MetricsManager metricsManager;
    private ModuleManager moduleManager;
    private PlayerManager playerManager;
    private NexusClient client;

    private Nexus() {
        this.platform = null;
        this.workingDirectory = null;
        this.properties = null;
        this.eventManager = null;
        this.metricsManager = null;
        this.moduleManager = null;
        this.playerManager = null;
        this.client = null;
    }

    public static void loadNexus(NexusPlatform platform, File workingDirectory) {
        INSTANCE.platform = platform;
        INSTANCE.workingDirectory = workingDirectory;
        INSTANCE.properties = new Properties();

        String prefixFormat = "dd'_'M'_'yyyy'_'hh'_'mm'_'ss";
        SimpleDateFormat format = new SimpleDateFormat(prefixFormat);
        String date = format.format(new Date());
        NexusLogger.initialise(new File(workingDirectory, "nexus_%s.log".formatted(date)), "yyyy-MM-dd HH:mm:ss");
        NexusLogger.inform("Loading Nexus...", NexusLogger.LogType.COMMON);

        INSTANCE.load();
        INSTANCE.eventManager = new EventManager();
        INSTANCE.moduleManager = new ModuleManager();
        INSTANCE.playerManager = new PlayerManager();
        INSTANCE.client = new NexusClient();

        NexusLogger.inform("Nexus loaded successfully for platform: %s", NexusLogger.LogType.COMMON, platform.getPlatformType().name());
    }

    private void load() {
        try {
            properties.load(ResourceLoader.getResource("nexus.properties"));
        } catch (IOException ex) {
            NexusLogger.error("Failed to load nexus.properties", ex);
        }
    }

    public static void postLoad() {
        INSTANCE.metricsManager = new MetricsManager(INSTANCE.platform.getConfiguration().getBoolean("metrics"));
    }

    public static NexusPlatform getPlatform() throws NexusInitException {
        if (INSTANCE.platform == null)
            throw new NexusInitException("Nexus has not been initialised yet!");
        return INSTANCE.platform;
    }

    public static Properties getProperties() throws NexusInitException {
        if (INSTANCE.properties == null)
            throw new NexusInitException("Nexus has not been initialised yet!");
        return INSTANCE.properties;
    }

    public static EventManager getEventManager() {
        if (INSTANCE.eventManager == null)
            throw new NexusInitException("Nexus has not been initialised yet!");
        return INSTANCE.eventManager;
    }

    public static ModuleManager getModuleManager() {
        if (INSTANCE.moduleManager == null)
            throw new NexusInitException("Nexus has not been initialised yet!");
        return INSTANCE.moduleManager;
    }

    public static PlayerManager getPlayerManager() {
        if (INSTANCE.playerManager == null)
            throw new NexusInitException("Nexus has not been initialised yet!");
        return INSTANCE.playerManager;
    }

    public NexusClient getClient() {
        return client;
    }
}