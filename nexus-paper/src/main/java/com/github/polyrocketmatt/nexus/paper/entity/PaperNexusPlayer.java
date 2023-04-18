package com.github.polyrocketmatt.nexus.paper.entity;

import com.github.polyrocketmatt.nexus.api.entity.NexusPlayer;
import com.github.polyrocketmatt.nexus.paper.PaperNexus;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PaperNexusPlayer implements NexusPlayer {

    private final Player player;
    private final UUID uuid;

    public PaperNexusPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();

        //  Schedule a task to check the latest player data from the database (or create a new one if it doesn't exist)
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return uuid;
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "%s %s"
                .formatted(PaperNexus.getInstance().getPrefix(), message)));
    }

    @Override
    public String json() {
        return null;
    }
}
