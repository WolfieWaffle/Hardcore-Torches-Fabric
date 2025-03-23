package com.github.wolfiewaffle.hardcore_torches.loot;

import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProviderType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonSerializer;

public class HCTLootNumberProviderTypes {
    public static final LootNumberProviderType FAT = register("hardcore_torches:fat", new FatLootNumberProvider.Serializer());

    private static LootNumberProviderType register(String id, JsonSerializer<? extends LootNumberProvider> jsonSerializer) {
        return Registry.register(Registries.LOOT_NUMBER_PROVIDER_TYPE, new Identifier(id), new LootNumberProviderType(jsonSerializer));
    }

    public static void loadThisClass() {}
}