package com.github.polyrocketmatt.nexus.api.module;

public enum NexusModuleType {

    //  Modules used to handle player connection events
    CLIENT_DETECTION("client-detection"),
    CLIENT_CONNECTION("client-connection");

    private final String key;

    NexusModuleType(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

}
