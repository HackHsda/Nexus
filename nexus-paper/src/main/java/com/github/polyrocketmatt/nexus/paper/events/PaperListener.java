package com.github.polyrocketmatt.nexus.paper.events;

import com.github.polyrocketmatt.nexus.api.events.NexusListener;
import com.github.polyrocketmatt.nexus.paper.PaperNexus;
import org.bukkit.event.Listener;

public abstract class PaperListener extends NexusListener implements Listener {

    public PaperListener() {
        PaperNexus.getInstance().getServer().getPluginManager().registerEvents(this, PaperNexus.getInstance());
    }

}
