package com.github.polyrocketmatt.nexus.common.utils.processor;

import com.github.polyrocketmatt.nexus.common.Nexus;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class StringProcessor implements Processor {

    private static final StringProcessor instance = new StringProcessor();

    public StringProcessor() {}

    public @NotNull Set<String> contains(String string, Set<String> strings, char split, long maxRuntime) {
        Set<String> result = new HashSet<>();

        //  Split the string with 32 overlapping characters
        String[] splitString = string.split(String.valueOf(split));

        //  Get a new executor
        ExecutorService service = getService();

        //  Iterate through the split string
        for (String str : splitString) {
            service.submit(() -> {
                //  Iterate through the strings to check
                for (String check : strings)
                    if (str.contains(check))
                        result.add(check);
            });
        }

        //  Handle service termination
        Nexus.getThreadManager().handleTermination(service, 10000);

        return result;
    }

    private ExecutorService getService() {
        return Nexus.getThreadManager().getService(4);
    }

    public static StringProcessor getProcessor() {
        return instance;
    }
}
