package com.github.polyrocketmatt.nexus.common.command;

import com.github.polyrocketmatt.nexus.api.entity.NexusCommunicativeEntity;

public class ExportCommand extends NexusCommand {

    public ExportCommand() {
        super("export", "Exports the current state of Nexus to the log file.",
                new String[] { }, "nexus.command.export");
    }

    @Override
    public void run(NexusCommunicativeEntity commander, String[] args) throws Exception {

    }

}
