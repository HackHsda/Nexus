package com.github.polyrocketmatt.nexus.common.entity;

public record NexusPlayerConnection(String ip,
                                    boolean tor,
                                    boolean prox,
                                    boolean dc,
                                    boolean anon,
                                    boolean atk,
                                    boolean abr,
                                    boolean thr) { }
