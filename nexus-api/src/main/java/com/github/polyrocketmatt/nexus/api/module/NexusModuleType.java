package com.github.polyrocketmatt.nexus.api.module;

public enum NexusModuleType {

    //  Module that is used to handle player connection events
    CLIENT_DETECTION("client-detection");

    private final String key;

    NexusModuleType(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

}
