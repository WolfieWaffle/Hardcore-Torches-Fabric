//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.compat;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import dev.emi.trinkets.api.TrinketsApi;

public class TrinketsCompat {
  public TrinketsCompat() {
  }

  public static void registerTrinkets() {
    TrinketsApi.registerTrinket(Mod.LIT_LANTERN.asItem(), new LanternTrinket());
  }
}
