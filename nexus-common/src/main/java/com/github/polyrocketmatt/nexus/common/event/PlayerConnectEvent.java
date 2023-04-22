package com.github.polyrocketmatt.nexus.common.event;

import com.github.polyrocketmatt.nexus.api.events.NexusEvent;

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

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public InetAddress getAddress() {
        return address;
    }

    public boolean isLocal() {
        return address.getHostAddress().startsWith("0:0:0:0") || address.getHostAddress().startsWith("0:0:0:0:0:0:1");
    }

}
