package com.github.polyrocketmatt.nexus.common.manager;

import com.github.polyrocketmatt.nexus.api.manager.NexusManager;
import com.github.polyrocketmatt.nexus.api.metrics.NexusMetrics;
import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

public class MetricsManager implements NexusManager {

    private final NexusMetrics metrics;

    public MetricsManager(boolean enabled) {
        this.metrics = enabled ? Nexus.getPlatform().initMetrics() : null;
        if (enabled)
            NexusLogger.inform("Initialised %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
    }

    @Override
    public void close() {
        NexusLogger.inform("Closed %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
    }

    @Override
    public void log() {

    }

    public NexusMetrics getMetrics() {
        return metrics;
    }
}
