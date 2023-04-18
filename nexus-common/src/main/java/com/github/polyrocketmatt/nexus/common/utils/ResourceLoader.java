package com.github.polyrocketmatt.nexus.common.utils;

import java.io.InputStream;

public class ResourceLoader {

    private static final ResourceLoader INSTANCE = new ResourceLoader();
    private final ClassLoader classLoader = getClass().getClassLoader();

    public static InputStream getResource(String path) {
        return INSTANCE.classLoader.getResourceAsStream(path);
    }

}
