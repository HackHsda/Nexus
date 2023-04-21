package com.github.polyrocketmatt.nexus.paper.events.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.polyrocketmatt.nexus.api.manager.NexusManager;
import com.github.polyrocketmatt.nexus.common.exception.NexusException;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PaperPacketManager implements NexusManager {

    private final ProtocolManager manager;
    private final Set<PaperPacketListener> handlers;

    public PaperPacketManager() {
        this.manager = ProtocolLibrary.getProtocolManager();
        this.handlers = new HashSet<>();
    }

    public void register(PaperPacketListener handler) {
        if (handlers.contains(handler))
            throw new NexusException("Attempted to register a handler that is already registered: %s"
                    .formatted(handler.getClass().getSimpleName()));
        handlers.add(handler);
        manager.addPacketListener(handler.getAdapter());

        NexusLogger.inform("Registered handler: %s".formatted(handler.getClass().getSimpleName()), NexusLogger.LogType.COMMON);
    }

    public void unregister(PaperPacketListener handler) {
        if (!handlers.contains(handler))
            throw new NexusException("Attempted to unregister a handler that is not registered: %s"
                    .formatted(handler.getClass().getSimpleName()));
        handlers.remove(handler);
        manager.removePacketListener(handler.getAdapter());

        NexusLogger.inform("Unregistered handler: %s".formatted(handler.getClass().getSimpleName()), NexusLogger.LogType.COMMON);
    }

    public Set<PaperPacketListener> getHandler(PacketType type) {
        return handlers.stream().filter(protocol -> protocol.hasType(type)).collect(Collectors.toSet());
    }

    @Override
    public void close() {
        handlers.forEach(this::unregister);
    }

    @Override
    public void log() {

    }
}
