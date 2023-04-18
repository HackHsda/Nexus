package com.github.polyrocketmatt.nexus.common.client;

import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;

import java.util.concurrent.TimeUnit;

public class NexusClient {

    private static final long DAY_DURATION = TimeUnit.HOURS.toMillis(24);

    private final String url;
    private final long pushesPerDay;
    private final long pullsPerDay;

    public NexusClient() {
        this.url = Nexus.getProperties().getProperty("network.rest.url");
        this.pushesPerDay = DAY_DURATION / Integer.parseInt(Nexus.getProperties().getProperty("network.rest.max-push"));
        this.pullsPerDay = DAY_DURATION / Integer.parseInt(Nexus.getProperties().getProperty("network.rest.max-pull"));

        NexusLogger.inform("Client has been configured with the following parameters:", NexusLogger.LogType.COMMON);
        NexusLogger.inform("    URL: %s".formatted(this.url), NexusLogger.LogType.COMMON);
        NexusLogger.inform("    Pushes Per Day: %d".formatted(this.pushesPerDay), NexusLogger.LogType.COMMON);
        NexusLogger.inform("    Pulls Per Day: %d".formatted(this.pullsPerDay), NexusLogger.LogType.COMMON);
    }

    public String getUrl() {
        return url;
    }

    public long getPushesPerDay() {
        return pushesPerDay;
    }

    public long getPullsPerDay() {
        return pullsPerDay;
    }
}
