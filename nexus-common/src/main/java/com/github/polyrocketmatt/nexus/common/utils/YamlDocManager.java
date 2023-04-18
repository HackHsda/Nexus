package com.github.polyrocketmatt.nexus.common.utils;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class YamlDocManager {

    public static YamlDocument from(InputStream is, String name) {
        try {
            File tempFile = File.createTempFile("properties", ".tmp");
            OutputStream os = new FileOutputStream(tempFile);

            tempFile.deleteOnExit();
            os.write(is.readAllBytes());
            os.close();

            return YamlDocument.create(tempFile);
        } catch (FileNotFoundException ex) {
            NexusLogger.error("Failed to find file document: %s".formatted(ex.getMessage()), NexusLogger.LogType.COMMON);
            NexusLogger.error("    Message: %s".formatted(ex.getMessage()), NexusLogger.LogType.COMMON);
            NexusLogger.error("    Stack Trace: %s".formatted(Arrays.toString(ex.getStackTrace())), NexusLogger.LogType.COMMON);
        } catch (IOException ex) {
            NexusLogger.error("Failed to write temp document: %s".formatted(ex.getMessage()), NexusLogger.LogType.COMMON);
            NexusLogger.error("    Message: %s".formatted(ex.getMessage()), NexusLogger.LogType.COMMON);
            NexusLogger.error("    Stack Trace: %s".formatted(Arrays.toString(ex.getStackTrace())), NexusLogger.LogType.COMMON);
        }

        throw new NullPointerException("Failed to load YAML document: %s".formatted(name));
    }

    public static YamlDocument get(File folder, String name) {
        try {
            return YamlDocument.create(new File(folder, name + ".yml"));
        } catch (IOException ex) {
            NexusLogger.error("Failed to load YAML document: %s".formatted(ex.getMessage()), NexusLogger.LogType.COMMON);
            NexusLogger.error("    Message: %s".formatted(ex.getMessage()), NexusLogger.LogType.COMMON);
            NexusLogger.error("    Stack Trace: %s".formatted(Arrays.toString(ex.getStackTrace())), NexusLogger.LogType.COMMON);
        }

        throw new NullPointerException("Failed to load YAML document: %s".formatted(name));
    }

    public static void save(YamlDocument document) {
        try {
            document.save();
            document.update();
        } catch (IOException ex) {
            NexusLogger.error("Failed to save and update YAML document: %s".formatted(ex.getMessage()), NexusLogger.LogType.COMMON);
            NexusLogger.error("    Message: %s".formatted(ex.getMessage()), NexusLogger.LogType.COMMON);
            NexusLogger.error("    Stack Trace: %s".formatted(Arrays.toString(ex.getStackTrace())), NexusLogger.LogType.COMMON);
        }
    }

}
