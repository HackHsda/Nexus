package com.github.polyrocketmatt.nexus.api.scheduling;

import io.netty.util.TimerTask;

import java.util.UUID;

public interface NexusTask extends TimerTask {

    UUID getTaskId();

}
