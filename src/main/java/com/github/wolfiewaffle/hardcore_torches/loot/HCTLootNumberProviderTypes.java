package com.github.wolfiewaffle.hardcore_torches.loot;

import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProviderType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.registry.Registry;

public class HCTLootNumberProviderTypes {
    public static final LootNumberProviderType FAT = register("hardcore_torches:fat", new FatLootNumberProvider.Serializer());

    private static LootNumberProviderType register(String id, JsonSerializer<? extends LootNumberProvider> jsonSerializer) {
        return Registry.register(Registry.LOOT_NUMBER_PROVIDER_TYPE, new Identifier(id), new LootNumberProviderType(jsonSerializer));
    }

    public static void loadThisClass() {}
}
