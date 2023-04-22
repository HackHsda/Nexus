package com.github.polyrocketmatt.nexus.common.manager;

import com.github.polyrocketmatt.nexus.api.entity.NexusCommunicativeEntity;
import com.github.polyrocketmatt.nexus.api.manager.NexusManager;
import com.github.polyrocketmatt.nexus.common.command.NexusCommand;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CommandManager implements NexusManager {

    private final Set<NexusCommand> commands;

    public CommandManager() {
        this.commands = new HashSet<>();
    }

    @Override
    public void close() {
        this.commands.clear();
    }

    @Override
    public void log() {
        NexusLogger.inform("Registered %s commands".formatted(this.commands.size()));

        commands.forEach(command -> NexusLogger.inform("     - %s".formatted(command.getName())));
    }

    public CommandManager registerCommand(NexusCommand command) {
        this.commands.add(command);
        return this;
    }

    public void processCommand(NexusCommunicativeEntity commander, String command, String[] args) {
        if (command.equalsIgnoreCase("nexus") || command.equalsIgnoreCase("nx")) {
            if (args.length == 0) {
                commander.sendMessage("&bNexus Commands");

                for (NexusCommand nexusCommand : this.commands)
                    if (commander.hasPermission(nexusCommand.getPermission()))
                        commander.sendMessage(nexusCommand.getUsage());
            } else {
                String commandName = args[0];
                String[] commandArgs = new String[args.length - 1];
                System.arraycopy(args, 1, commandArgs, 0, commandArgs.length);

                for (NexusCommand nexusCommand : this.commands) {
                    if (nexusCommand.getName().equalsIgnoreCase(commandName)) {
                        if (nexusCommand.getArguments().length != commandArgs.length)
                            commander.sendMessage("&cIncorrect number of arguments. Expected %s, got %s".formatted(nexusCommand.getArguments().length, commandArgs.length));
                        else if (!commander.hasPermission(nexusCommand.getPermission()))
                            commander.sendMessage("&cYou do not have permission to run this command!");
                        else
                            try {
                                nexusCommand.run(commander, commandArgs);
                            } catch (Exception ex) {
                                String trace =  Arrays.toString(ex.getStackTrace());

                                NexusLogger.error("An error occurred whilst running command '%s'".formatted(nexusCommand.getName()), NexusLogger.LogType.COMMON);
                                NexusLogger.error("    Type: %s".formatted(ex.getClass().getSimpleName()), NexusLogger.LogType.COMMON);
                                NexusLogger.error("    Message: %s".formatted(ex.getMessage()), NexusLogger.LogType.COMMON);
                                NexusLogger.error("    Stack Trace: %s".formatted(trace), NexusLogger.LogType.COMMON);
                            }
                    }
                }
            }
        }
    }

}
