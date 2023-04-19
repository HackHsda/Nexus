package com.github.polyrocketmatt.nexus.api.scheduling;

import java.util.TimerTask;
import java.util.UUID;

public abstract class NexusTask extends TimerTask {

    private final UUID taskId;
    private final long delay;
    private final long period;
    private boolean isRunning = true;

    public NexusTask(UUID uuid, long delay, long period) {
        this.taskId = uuid;
        this.delay = delay;
        this.period = period;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public long getDelay() {
        return delay;
    }

    public long getPeriod() {
        return period;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
