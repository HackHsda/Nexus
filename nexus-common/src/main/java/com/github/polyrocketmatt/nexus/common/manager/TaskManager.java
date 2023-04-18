package com.github.polyrocketmatt.nexus.common.manager;

import com.github.polyrocketmatt.nexus.api.scheduling.NexusTask;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class TaskManager extends Thread {

    private final Map<NexusTask, Timer> taskSet;

    public TaskManager() {
        this.taskSet = new HashMap<>();
    }

    public void addTask(NexusTask task, long delay, long period) {
        Timer taskTimer = new Timer();
        taskTimer.schedule((TimerTask) task, delay, period);
        taskSet.put(task, taskTimer);
    }

    public @Nullable NexusTask getTask(UUID uuid) {
        return taskSet.keySet().stream().filter(task -> task.getTaskId().equals(uuid)).findFirst().orElse(null);
    }

    public @Nullable Timer getTaskTimer(UUID uuid) {
        return taskSet.entrySet()
                .stream()
                .filter(entry -> entry.getKey().getTaskId().equals(uuid))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    public void cancel(UUID uuid) {
        taskSet.entrySet()
                .stream()
                .filter(entry -> entry.getKey().getTaskId().equals(uuid))
                .findFirst()
                .ifPresent(entry -> {
                    entry.getValue().cancel();
                    taskSet.remove(entry.getKey());
                });
    }

    public void cancelAll() {
        taskSet.forEach((task, timer) -> timer.cancel());
    }

}