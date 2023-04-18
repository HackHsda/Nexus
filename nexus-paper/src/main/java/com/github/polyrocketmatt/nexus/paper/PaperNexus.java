package com.github.polyrocketmatt.nexus.paper;

import com.github.polyrocketmatt.nexus.api.NexusPlatform;
import com.github.polyrocketmatt.nexus.api.PlatformType;
import com.github.polyrocketmatt.nexus.api.events.NexusListener;
import com.github.polyrocketmatt.nexus.api.metrics.NexusMetrics;
import com.github.polyrocketmatt.nexus.api.module.NexusModule;
import com.github.polyrocketmatt.nexus.api.module.NexusModuleType;
import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.entity.NexusPlayer;
import com.github.polyrocketmatt.nexus.common.exception.NexusEntityException;
import com.github.polyrocketmatt.nexus.common.exception.NexusException;
import com.github.polyrocketmatt.nexus.common.exception.NexusModuleException;
import com.github.polyrocketmatt.nexus.common.modules.ClientDetectionModule;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import com.github.polyrocketmatt.nexus.common.utils.ResourceLoader;
import com.github.polyrocketmatt.nexus.common.utils.YamlDocManager;
import com.github.polyrocketmatt.nexus.paper.entity.PaperNexusPlayer;
import com.github.polyrocketmatt.nexus.paper.events.PaperConnectionListener;
import com.github.polyrocketmatt.nexus.paper.metrics.PaperMetrics;
import com.github.polyrocketmatt.nexus.paper.packets.PaperCustomPayloadProtocol;
import com.github.polyrocketmatt.nexus.paper.packets.PaperPacketProtocol;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

public class PaperNexus extends JavaPlugin implements NexusPlatform {

    private static PaperNexus instance;

    private final File LOGGING_DIR = new File(getDataFolder(), "logging");
    private YamlDocument configuration;
    private PaperPacketProtocol protocol;

    public PaperNexus() { instance = this; }

    public static PaperNexus getInstance() {
        return instance;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onLoad() {
        boolean install = !getDataFolder().exists();
        if (!getDataFolder().exists())  getDataFolder().mkdir();
        if (!LOGGING_DIR.exists())      LOGGING_DIR.mkdir();

        //  Initialize Nexus
        Nexus.loadNexus(this, LOGGING_DIR);

        //  Initialize Configuration
        configuration = YamlDocManager.get(getDataFolder(), "config");

        //  Install if necessary
        if (install)
            install();

        //  Initialize ProtocolLib
        protocol = new PaperPacketProtocol();
    }

    @Override
    public void onEnable() {
        NexusLogger.inform("Enabling Nexus: Paper", NexusLogger.LogType.PLATFORM);

        //  Initialize Paper listeners
        registerListener(new PaperConnectionListener());

        //  Initialize Nexus Modules
        if (configuration.getBoolean("modules.client-detection.enabled"))
            registerModule(new ClientDetectionModule());

        //  Initialize Packet Protocols
        protocol.register(new PaperCustomPayloadProtocol());

        //  Post load
        Nexus.postLoad();
    }

    @Override
    public void onDisable() {
        NexusLogger.inform("Disabling Nexus: Paper", NexusLogger.LogType.PLATFORM);
        NexusLogger.shutdown();
    }

    @Override
    public @NotNull String getPrefix() {
        return configuration.getString("prefix");
    }

    @Override
    public @NotNull YamlDocument getConfiguration() {
        return configuration;
    }

    @Override
    public void registerListener(@NotNull NexusListener listener) {
        Nexus.getEventManager().registerListener(listener);
    }

    @Override
    public void registerModule(@NotNull NexusModule module) {
        Nexus.getModuleManager().registerModule(module);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <T extends NexusModule> T getModule(@NotNull NexusModuleType type) {
        T module = (T) Nexus.getModuleManager().getModule(type);
        if (module == null)
            throw new NexusModuleException("Module \"%s\" could not be found".formatted(type.name()), type);
        return module;
    }

    @Override
    public @NotNull NexusPlayer getPlayer(@NotNull UUID uuid) {
        NexusPlayer player = Nexus.getPlayerManager().getPlayer(uuid);
        if (player == null)
            throw new NexusEntityException("Player \"%s\" could not be found".formatted(uuid.toString()));
        return player;
    }

    @Override
    public void registerPlayer(@NotNull UUID uuid) {
        Player player = Objects.requireNonNull(Bukkit.getPlayer(uuid));
        Nexus.getPlayerManager().registerPlayer(new PaperNexusPlayer(player));
    }

    @Override
    public void unregisterPlayer(@NotNull UUID uuid) {
        Nexus.getPlayerManager().unregisterPlayer(uuid);
    }

    @Override
    public boolean getEnvBoolean(@NotNull String key) {
        try {
            return Boolean.parseBoolean(Nexus.getProperties().getProperty(key));
        }
        catch (NexusException ex) { return false; }
    }

    @Override
    public int getEnvInt(@NotNull String key) {
        try {
            return Integer.parseInt(Nexus.getProperties().getProperty(key));
        }
        catch (NexusException | NumberFormatException ex) { return 0; }
    }

    @Override
    public double getEnvDouble(@NotNull String key) {
        try {
            return Double.parseDouble(Nexus.getProperties().getProperty(key));
        } catch (NexusException | NumberFormatException ex) { return 0.0; }
    }

    @Override
    public String getEnvString(@NotNull String key) {
        try {
            return Nexus.getProperties().getProperty(key);
        } catch (NexusException ex) { return ""; }
    }

    @Override
    public @NotNull NexusMetrics initMetrics() {
        return new PaperMetrics(this, 18233);
    }

    @Override
    public @NotNull PlatformType getPlatformType() {
        return PlatformType.PAPER;
    }

    private void install() {
        YamlDocument localConfig = YamlDocManager.from(ResourceLoader.getResource("config.yml"), "config.yml");

        localConfig.getKeys().forEach(key -> configuration.set((String) key, localConfig.get((String) key)));

        YamlDocManager.save(configuration);
    }
}
