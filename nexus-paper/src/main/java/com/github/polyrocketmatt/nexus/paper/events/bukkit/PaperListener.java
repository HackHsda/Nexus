package com.github.polyrocketmatt.nexus.paper.events.bukkit;

import com.github.polyrocketmatt.nexus.api.events.ExternalEventListener;
import com.github.polyrocketmatt.nexus.paper.PaperNexus;
import org.bukkit.event.Listener;

public abstract class PaperListener implements ExternalEventListener, Listener {

    public PaperListener() {
        PaperNexus.getInstance().getServer().getPluginManager().registerEvents(this, PaperNexus.getInstance());
    }
}
