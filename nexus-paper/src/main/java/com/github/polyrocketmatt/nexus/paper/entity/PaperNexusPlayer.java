package com.github.polyrocketmatt.nexus.paper.entity;

import com.github.polyrocketmatt.nexus.common.entity.NexusPlayer;
import com.github.polyrocketmatt.nexus.common.entity.NexusPlayerData;
import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.client.NexusPlayerStatusClient;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import com.github.polyrocketmatt.nexus.paper.PaperNexus;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PaperNexusPlayer extends NexusPlayer {

    private final Player player;
    private NexusPlayerData playerData;

    public PaperNexusPlayer(Player player) {
        super(player.getUniqueId());
        this.player = player;

        //  Schedule a task to check the latest player data from the database (or create a new one if it doesn't exist)
        Nexus.getThreadManager().accept(
                () -> {
                    //  Get available client and perform GET
                    var client = new NexusPlayerStatusClient(this);
                    return client.get(NexusPlayerData.class);
                },
                data -> {
                    playerData = data;

                    NexusLogger.inform("Player data for %s has been loaded", NexusLogger.LogType.COMMON, player.getName());
                    NexusLogger.inform("    UUID: %s", NexusLogger.LogType.COMMON, uuid.toString());
                    NexusLogger.inform("    Data: %s", NexusLogger.LogType.COMMON, data.data());
                }
        );
    }

    @Override
    public void sendMessage(@NotNull String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "%s %s"
                .formatted(PaperNexus.getInstance().getPrefix(), message)));
    }

    @Override
    public NexusPlayerData getPlayerData() {
        return playerData;
    }
}
