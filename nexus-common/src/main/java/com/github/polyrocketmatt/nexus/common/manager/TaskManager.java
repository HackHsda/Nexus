package com.github.polyrocketmatt.nexus.common.manager;

import com.github.polyrocketmatt.nexus.api.manager.NexusManager;
import com.github.polyrocketmatt.nexus.api.scheduling.NexusTask;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;

public class TaskManager extends Thread implements NexusManager {

    private final Map<NexusTask, Timer> taskSet;

    public TaskManager() {
        this.taskSet = new HashMap<>();
    }

    @Override
    public void close() {
        cancelAllTasks();
        taskSet.clear();

        NexusLogger.inform("Closed %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
        NexusLogger.inform("    Closed Tasks: %s", NexusLogger.LogType.COMMON, taskSet.size());
    }

    public void removeTask(UUID uuid) {
        NexusTask task = getTask(uuid);
        if (task == null)
            return;
        task.cancel();
        taskSet.remove(task);
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

    public void scheduleTask(NexusTask task) {
        long delay = task.getDelay();
        long period = task.getPeriod();

        Timer taskTimer = new Timer();
        taskTimer.schedule(task, delay, period);
        taskSet.put(task, taskTimer);
    }

    public void rescheduleTask(UUID uuid) {
        NexusTask task = getTask(uuid);
        if (task == null)
            return;
        scheduleTask(task);
    }

    public void cancelTask(UUID uuid) {
        Timer timer = getTaskTimer(uuid);
        if (timer == null)
            return;
        timer.cancel();
    }

    public void cancelAllTasks() {
        taskSet.forEach((task, timer) -> timer.cancel());
    }

}
