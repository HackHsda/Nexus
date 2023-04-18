package com.github.polyrocketmatt.nexus.common.manager;

import com.github.polyrocketmatt.nexus.api.metrics.NexusMetrics;
import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

public class MetricsManager {

    private final NexusMetrics metrics;

    public MetricsManager(boolean enabled) {
        this.metrics = enabled ? Nexus.getPlatform().initMetrics() : null;
        if (enabled)
            NexusLogger.inform("Metrics have been enabled.", NexusLogger.LogType.COMMON);
    }

    public NexusMetrics getMetrics() {
        return metrics;
    }
}
