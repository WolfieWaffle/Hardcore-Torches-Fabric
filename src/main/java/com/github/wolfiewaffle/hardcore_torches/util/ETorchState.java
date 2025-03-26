//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.util;

import net.minecraft.util.StringIdentifiable;

public enum ETorchState implements StringIdentifiable {
  LIT("lit"),
  UNLIT("unlit"),
  SMOLDERING("smoldering"),
  BURNT("burnt");

  private final String name;

  private ETorchState(String name) {
    this.name = name;
  }

  public String toString() {
    return this.name;
  }

  public String asString() {
    return this.name;
  }
}
