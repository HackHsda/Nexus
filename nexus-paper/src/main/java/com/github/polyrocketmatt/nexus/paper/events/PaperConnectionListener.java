package com.github.polyrocketmatt.nexus.paper.events;

import com.github.polyrocketmatt.nexus.paper.PaperNexus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PaperConnectionListener extends PaperListener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        PaperNexus.getInstance().registerPlayer(uuid);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        PaperNexus.getInstance().unregisterPlayer(uuid);
    }

}
