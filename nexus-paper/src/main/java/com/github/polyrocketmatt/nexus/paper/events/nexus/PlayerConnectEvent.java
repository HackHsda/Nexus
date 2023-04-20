package com.github.polyrocketmatt.nexus.paper.events.nexus;

import com.github.polyrocketmatt.nexus.api.events.NexusEvent;
import com.github.polyrocketmatt.nexus.api.module.NexusModuleType;

import java.net.InetAddress;
import java.util.UUID;

public class PlayerConnectEvent extends NexusEvent {

    private final String name;
    private final String host;
    private final InetAddress address;

    public PlayerConnectEvent(UUID uuid, String name, String host, InetAddress address) {
        super(uuid);
        this.name = name;
        this.host = host;
        this.address = address;
    }

    @Override
    public NexusModuleType getModuleHandle() {
        return NexusModuleType.CLIENT_CONNECTION;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public InetAddress getAddress() {
        return address;
    }
}
