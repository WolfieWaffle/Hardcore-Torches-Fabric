package com.github.wolfiewaffle.hardcore_torches.util;

import net.minecraft.util.StringIdentifiable;

public enum ETorchState implements StringIdentifiable {
    LIT("lit"),
    UNLIT("unlit"),
    SMOLDERING("smoldering"),
    BURNT("burnt");

    private final String name;

    ETorchState(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
