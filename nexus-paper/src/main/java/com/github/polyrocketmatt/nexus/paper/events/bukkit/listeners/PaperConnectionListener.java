package com.github.polyrocketmatt.nexus.paper.events.bukkit.listeners;

import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.entity.NexusPlayer;
import com.github.polyrocketmatt.nexus.paper.PaperNexus;
import com.github.polyrocketmatt.nexus.paper.events.bukkit.PaperListener;
import com.github.polyrocketmatt.nexus.common.event.PlayerConnectEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        PlayerConnectEvent nexusEvent = new PlayerConnectEvent(uuid, event.getPlayer().getName(), event.getHostname(), event.getAddress());
        Nexus.getEventManager().dispatch(nexusEvent);
    }

}
