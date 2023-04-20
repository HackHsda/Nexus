package com.github.polyrocketmatt.nexus.common.manager;

import com.github.polyrocketmatt.nexus.api.manager.NexusManager;
import com.github.polyrocketmatt.nexus.api.scheduling.NexusTask;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import com.github.polyrocketmatt.nexus.common.utils.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;

public class TaskManager extends Thread implements NexusManager {

    private final Map<NexusTask, Pair<Timer, Boolean>> taskSet;

    public TaskManager() {
        this.taskSet = new HashMap<>();

        NexusLogger.inform("Initialised %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
    }

    @Override
    public void close() {
        cancelAllTasks();
        taskSet.clear();

        NexusLogger.inform("Closed %s", NexusLogger.LogType.COMMON, getClass().getSimpleName());
        NexusLogger.inform("    Closed Tasks: %s", NexusLogger.LogType.COMMON, taskSet.size());
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
                .map(Pair::getFirst)
                .orElse(null);
    }

    public boolean taskIsRunning(UUID uuid) {
        return taskSet.entrySet()
                .stream()
                .filter(entry -> entry.getKey().getTaskId().equals(uuid))
                .findFirst()
                .map(Map.Entry::getValue)
                .map(Pair::getSecond)
                .orElse(false);
    }

    public void scheduleTaskAtFixedRate(NexusTask task) {
        long delay = task.getDelay();
        long period = task.getPeriod();

        Timer taskTimer = new Timer();
        taskTimer.schedule(task, delay, period);
        taskSet.put(task, new Pair<>(taskTimer, true));
        task.setRunning(true);

        NexusLogger.inform("Scheduled task %s", NexusLogger.LogType.COMMON, task.getTaskId());
        NexusLogger.inform("    Delay: %s", NexusLogger.LogType.COMMON, task.getDelay());
        NexusLogger.inform("    Period: %s", NexusLogger.LogType.COMMON, task.getPeriod());
    }

    public void rescheduleTask(UUID uuid) {
        NexusTask task = getTask(uuid);
        if (task == null)
            return;
        scheduleTaskAtFixedRate(task);

        NexusLogger.inform("Rescheduled task %s", NexusLogger.LogType.COMMON, task.getTaskId());
    }

    public void cancelTask(UUID uuid) {
        NexusTask task = getTask(uuid);
        if (task == null)
            return;

        task.setRunning(false);
        task.cancel();

        NexusLogger.inform("Cancelled task %s", NexusLogger.LogType.COMMON, task.getTaskId());
    }

    public void cancelAllTasks() {
        taskSet.forEach((task, timer) -> task.setRunning(false));
        taskSet.forEach((task, timer) -> task.cancel());
    }

}
