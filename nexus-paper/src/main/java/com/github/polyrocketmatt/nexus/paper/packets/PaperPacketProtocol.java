package com.github.polyrocketmatt.nexus.paper.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.polyrocketmatt.nexus.common.exception.NexusException;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PaperPacketProtocol {

    private final ProtocolManager manager;
    private final Set<PaperProtocol> protocols;

    public PaperPacketProtocol() {
        this.manager = ProtocolLibrary.getProtocolManager();
        this.protocols = new HashSet<>();
    }

    public void register(PaperProtocol protocol) {
        if (protocols.contains(protocol))
            throw new NexusException("Attempted to register a protocol that is already registered: %s"
                    .formatted(protocol.getClass().getSimpleName()));
        protocols.add(protocol);
        manager.addPacketListener(protocol.getAdapter());

        NexusLogger.inform("Registered protocol: %s".formatted(protocol.getClass().getSimpleName()), NexusLogger.LogType.COMMON);
    }

    public void unregister(PaperProtocol protocol) {
        if (!protocols.contains(protocol))
            throw new NexusException("Attempted to unregister a protocol that is not registered: %s"
                    .formatted(protocol.getClass().getSimpleName()));
        protocols.remove(protocol);
        manager.removePacketListener(protocol.getAdapter());

        NexusLogger.inform("Unregistered protocol: %s".formatted(protocol.getClass().getSimpleName()), NexusLogger.LogType.COMMON);
    }

    public Set<PaperProtocol> getProtocol(PacketType type) {
        return protocols.stream().filter(protocol -> protocol.hasType(type)).collect(Collectors.toSet());
    }

}
