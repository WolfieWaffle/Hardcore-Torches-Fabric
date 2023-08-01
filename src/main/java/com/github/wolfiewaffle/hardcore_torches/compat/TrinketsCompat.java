package com.github.wolfiewaffle.hardcore_torches.compat;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import dev.emi.trinkets.api.TrinketsApi;

public class TrinketsCompat {

    public static void registerTrinkets() {
        TrinketsApi.registerTrinket(Mod.LIT_LANTERN.asItem(), new LanternTrinket());
    }

}
