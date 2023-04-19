package com.github.polyrocketmatt.nexus.common.utils.processor;

import com.github.polyrocketmatt.nexus.common.Nexus;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class StringProcessor implements Processor {

    private static final StringProcessor instance = new StringProcessor();

    public StringProcessor() {}

    public @NotNull Set<String> contains(String string, Set<String> strings, char split) {
        Set<String> result = new HashSet<>();

        //  Split the string with 32 overlapping characters
        String[] splitString = string.split(String.valueOf(split));

        //  Iterate through the split string
        for (String str : splitString) {
            Nexus.getThreadManager().submit(() -> {
                //  Iterate through the strings to check
                for (String check : strings)
                    if (str.contains(check))
                        result.add(check);
            });
        }

        return result;
    }

    public static StringProcessor getProcessor() {
        return instance;
    }
}
