package com.github.polyrocketmatt.nexus.common.command;

import com.github.polyrocketmatt.nexus.api.entity.NexusCommunicativeEntity;

public abstract class NexusCommand {

    protected String name;
    protected String description;
    protected String[] arguments;
    protected String permission;

    public NexusCommand(String name, String description, String[] arguments, String permission) {
        this.name = name;
        this.description = description;
        this.arguments = arguments;
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getArguments() {
        return arguments;
    }

    public String getPermission() {
        return permission;
    }

    public String getUsage() {
        return "/%s %s - %s".formatted(name, String.join(" ", arguments), description);
    }

    public abstract void run(NexusCommunicativeEntity commander, String[] args) throws Exception;

}
