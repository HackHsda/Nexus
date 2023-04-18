package com.github.polyrocketmatt.nexus.common.utils;

public record Pair<A, B>(A a, B b) {

    public A getFirst() {
        return a;
    }

    public B getSecond() {
        return b;
    }

}
