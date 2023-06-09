package com.github.polyrocketmatt.nexus.paper.entity;

import com.github.polyrocketmatt.nexus.common.entity.NexusPlayer;
import com.github.polyrocketmatt.nexus.paper.PaperNexus;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PaperNexusPlayer extends NexusPlayer {

    private final Player player;

    public PaperNexusPlayer(Player player) {
        super(player.getUniqueId(), Objects.requireNonNull(player.getAddress().getAddress().getHostAddress()));
        this.player = player;

        System.out.println("Address: " + player.getAddress().toString());
    }

    @Override
    public void sendMessage(@NotNull String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "%s %s"
                .formatted(PaperNexus.getInstance().getPrefix(), message)));
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return player.hasPermission(permission) || player.isOp();
    }
}
