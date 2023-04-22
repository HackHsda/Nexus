package com.github.polyrocketmatt.nexus.paper.events.bukkit.listeners;

import com.github.polyrocketmatt.nexus.paper.events.bukkit.PaperListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PaperCommandListener extends PaperListener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        System.out.println(event.getMessage());
    }

}
