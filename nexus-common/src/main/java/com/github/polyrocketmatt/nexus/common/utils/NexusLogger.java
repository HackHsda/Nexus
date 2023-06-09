package com.github.polyrocketmatt.nexus.common.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

public class NexusLogger extends Thread {

    private static NexusLogger logger;

    private final SimpleDateFormat format;
    private final File logFile;
    private final BufferedWriter writer;
    private final Semaphore semaphore;
    private final Timer timer;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private NexusLogger(File logFile, String format) {
        try {
            if (!logFile.exists())
                logFile.createNewFile();

            this.format = new SimpleDateFormat(format);
            this.logFile = logFile;
            this.writer = new BufferedWriter(new FileWriter(logFile, true));
            this.semaphore = new Semaphore(1);
            this.timer = new Timer();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to create logger for file " + logFile.getName(), ex);
        }
    }

    @Override
    public void run() {
        TimerTask flushTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    flush();
                } catch (Exception ex) {
                    throw new RuntimeException("Unable to flush log file " + logFile.getName(), ex);
                }
            }
        };

        timer.scheduleAtFixedRate(flushTask, 0, 10000);
    }

    private synchronized void writeLog(String message) {
        try {
            writer.write(message);
            writer.flush();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to write to log file " + logFile.getName(), ex);
        }
    }

    private void logInform(String message) {
        this.logInform(message, LogType.UNKNOWN);
    }

    private void logInform(String message, LogType type) {
        this.writeLog("%s [%s] [%s] %s\n".formatted(format.format(new Date()), LogLevel.INFO, type.name(), message));
    }

    private void logWarn(String message) {
        this.logWarn(message, LogType.UNKNOWN);
    }

    private void logWarn(String message, LogType type) {
        this.writeLog("%s [%s] [%s] %s\n".formatted(format.format(new Date()), LogLevel.WARNING, type.name(), message));
    }

    private void logError(String message) {
        this.logError(message, LogType.UNKNOWN);
    }

    private void logError(String message, LogType type) {
        this.writeLog("%s [%s] [%s] %s\n".formatted(format.format(new Date()), LogLevel.ERROR, type.name(), message));
    }

    private void waitAndWrite(LogLevel level, String message) throws InterruptedException {
        semaphore.acquire();
        switch (level) {
            case INFO ->        this.logInform(message);
            case WARNING ->     this.logWarn(message);
            case ERROR ->       this.logError(message);
        }
        semaphore.release();
    }

    private void waitAndWrite(LogLevel level, String[] messages) throws InterruptedException {
        semaphore.acquire();
        switch (level) {
            case INFO ->        Arrays.stream(messages).forEach(this::logInform);
            case WARNING ->     Arrays.stream(messages).forEach(this::logWarn);
            case ERROR ->       Arrays.stream(messages).forEach(this::logError);
        }
        semaphore.release();
    }

    private void waitAndWrite(LogLevel level, String message, LogType type) throws InterruptedException {
        semaphore.acquire();
        switch (level) {
            case INFO ->        this.logInform(message, type);
            case WARNING ->     this.logWarn(message, type);
            case ERROR ->       this.logError(message, type);
        }
        semaphore.release();
    }

    private void waitAndWrite(LogLevel level, String[] messages, LogType type) throws InterruptedException {
        semaphore.acquire();
        switch (level) {
            case INFO ->        Arrays.stream(messages).forEach(message -> this.logInform(message, type));
            case WARNING ->     Arrays.stream(messages).forEach(message -> this.logWarn(message, type));
            case ERROR ->       Arrays.stream(messages).forEach(message -> this.logError(message, type));
        }
        semaphore.release();
    }

    private void flush() {
        try {
            semaphore.acquire();
            writer.flush();
            semaphore.release();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to flush log file " + logFile.getName(), ex);
        }
    }

    private void close() {
        inform("Closing logger", LogType.COMMON);

        try {
            semaphore.acquire();
            writer.flush();
            writer.close();
            timer.cancel();
            semaphore.release();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to close log file " + logFile.getName(), ex);
        }

        this.interrupt();
    }

    public enum LogLevel {
        INFO,
        WARNING,
        ERROR
    }

    public enum LogType {
        UNKNOWN,
        COMMON,
        LIB,
        PLATFORM
    }

    public static void initialise(File logFile, String format) {
        //  This should never happen, but if it happens, we need to close the old logger
        if (logger != null)
            logger.close();

        logger = new NexusLogger(logFile, format);
        logger.start();

        inform("Initialised logger", LogType.COMMON);
    }

    private static void write(LogLevel level, String message) {
        try {
            logger.waitAndWrite(level, message);
        } catch (InterruptedException ex) {
            throw new RuntimeException("Unable to write to log file " + logger.logFile.getName(), ex);
        }
    }

    private static void write(LogLevel level, String[] messages) {
        try {
            logger.waitAndWrite(level, messages);
        } catch (InterruptedException ex) {
            throw new RuntimeException("Unable to write to log file " + logger.logFile.getName(), ex);
        }
    }

    private static void write(LogLevel level, String message, LogType type) {
        try {
            logger.waitAndWrite(level, message, type);
        } catch (InterruptedException ex) {
            throw new RuntimeException("Unable to write to log file " + logger.logFile.getName(), ex);
        }
    }

    private static void write(LogLevel level, String[] messages, LogType type) {
        try {
            logger.waitAndWrite(level, messages, type);
        } catch (InterruptedException ex) {
            throw new RuntimeException("Unable to write to log file " + logger.logFile.getName(), ex);
        }
    }

    public static void inform(String message) {
        write(LogLevel.INFO, message);
    }

    public static void inform(String message, LogType type) {
        write(LogLevel.INFO, message, type);
    }

    public static void inform(String[] messages) {
        write(LogLevel.INFO, messages);
    }

    public static void inform(String[] messages, LogType type) {
        write(LogLevel.INFO, messages, type);
    }

    public static void inform(String message, Object... args) {
        write(LogLevel.INFO, message.formatted(args));
    }

    public static void inform(String message, LogType type, Object... args) {
        write(LogLevel.INFO, message.formatted(args), type);
    }

    public static void warn(String message) {
        write(LogLevel.WARNING, message);
    }

    public static void warn(String message, LogType type) {
        write(LogLevel.WARNING, message, type);
    }

    public static void warn(String[] messages) {
        write(LogLevel.WARNING, messages);
    }

    public static void warn(String[] messages, LogType type) {
        write(LogLevel.WARNING, messages, type);
    }

    public static void warn(String message, Object... args) {
        write(LogLevel.WARNING, message.formatted(args));
    }

    public static void warn(String message, LogType type, Object... args) {
        write(LogLevel.WARNING, message.formatted(args), type);
    }

    public static void error(String message) {
        write(LogLevel.ERROR, message);
    }

    public static void error(String message, LogType type) {
        write(LogLevel.ERROR, message, type);
    }

    public static void error(String[] messages) {
        write(LogLevel.ERROR, messages);
    }

    public static void error(String[] messages, LogType type) {
        write(LogLevel.ERROR, messages, type);
    }

    public static void error(String message, Object... args) {
        write(LogLevel.ERROR, message.formatted(args));
    }

    public static void error(String message, LogType type, Object... args) {
        write(LogLevel.ERROR, message.formatted(args), type);
    }

    public static void shutdown() {
        logger.close();
    }

    public static long getThreadId() {
        return logger.getId();
    }

}
