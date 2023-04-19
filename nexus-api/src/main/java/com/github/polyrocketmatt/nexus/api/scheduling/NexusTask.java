package com.github.polyrocketmatt.nexus.api.scheduling;

import java.util.TimerTask;
import java.util.UUID;

public abstract class NexusTask extends TimerTask {

    public abstract UUID getTaskId();

}
