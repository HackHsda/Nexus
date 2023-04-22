package com.github.polyrocketmatt.nexus.api.events;

@FunctionalInterface
public interface InternalEventListener {

    void handle(InternalNexusEvent event);

}
