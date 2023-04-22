package com.github.polyrocketmatt.nexus.common.entity;

import java.util.List;
import java.util.UUID;

public record NexusPlayerData(UUID uuid,
                              List<NexusPlayerConnection> connections
) { }
