//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.loot;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;

public class HCTLootTableModifier {
    public HCTLootTableModifier() {
    }

    public static void registerLootPools() {
        LootTableEvents.MODIFY.register((LootTableEvents.Modify)(resourceManager, lootManager, id, tableBuilder, source) -> {
            if (EntityType.CHICKEN.getLootTableId().equals(id)) {
                LootPool.Builder pool = LootPool.builder().rolls(new FatLootNumberProvider(new int[]{0, 0, 0, 1})).with(ItemEntry.builder(Mod.ANIMAL_FAT));
                tableBuilder.pool(pool);
            }

        });
        LootTableEvents.MODIFY.register((LootTableEvents.Modify)(resourceManager, lootManager, id, tableBuilder, source) -> {
            if (EntityType.COW.getLootTableId().equals(id)) {
                LootPool.Builder pool = LootPool.builder().rolls(new FatLootNumberProvider(new int[]{0, 1, 1, 1})).with(ItemEntry.builder(Mod.ANIMAL_FAT));
                tableBuilder.pool(pool);
            }

        });
        LootTableEvents.MODIFY.register((LootTableEvents.Modify)(resourceManager, lootManager, id, tableBuilder, source) -> {
            if (EntityType.GOAT.getLootTableId().equals(id)) {
                LootPool.Builder pool = LootPool.builder().rolls(new FatLootNumberProvider(new int[]{0, 0, 0, 1})).with(ItemEntry.builder(Mod.ANIMAL_FAT));
                tableBuilder.pool(pool);
            }

        });
        LootTableEvents.MODIFY.register((LootTableEvents.Modify)(resourceManager, lootManager, id, tableBuilder, source) -> {
            if (EntityType.HOGLIN.getLootTableId().equals(id)) {
                LootPool.Builder pool = LootPool.builder().rolls(new FatLootNumberProvider(new int[]{1, 3, 5, 7})).with(ItemEntry.builder(Mod.ANIMAL_FAT));
                tableBuilder.pool(pool);
            }

        });
        LootTableEvents.MODIFY.register((LootTableEvents.Modify)(resourceManager, lootManager, id, tableBuilder, source) -> {
            if (EntityType.HORSE.getLootTableId().equals(id)) {
                LootPool.Builder pool = LootPool.builder().rolls(new FatLootNumberProvider(new int[]{0, 0, 0, 1})).with(ItemEntry.builder(Mod.ANIMAL_FAT));
                tableBuilder.pool(pool);
            }

        });
        LootTableEvents.MODIFY.register((LootTableEvents.Modify)(resourceManager, lootManager, id, tableBuilder, source) -> {
            if (EntityType.MOOSHROOM.getLootTableId().equals(id)) {
                LootPool.Builder pool = LootPool.builder().rolls(new FatLootNumberProvider(new int[]{0, 1, 1, 1})).with(ItemEntry.builder(Mod.ANIMAL_FAT));
                tableBuilder.pool(pool);
            }

        });
        LootTableEvents.MODIFY.register((LootTableEvents.Modify)(resourceManager, lootManager, id, tableBuilder, source) -> {
            if (EntityType.PIG.getLootTableId().equals(id)) {
                LootPool.Builder pool = LootPool.builder().rolls(new FatLootNumberProvider(new int[]{0, 1, 2, 2})).with(ItemEntry.builder(Mod.ANIMAL_FAT));
                tableBuilder.pool(pool);
            }

        });
        LootTableEvents.MODIFY.register((LootTableEvents.Modify)(resourceManager, lootManager, id, tableBuilder, source) -> {
            if (EntityType.SHEEP.getLootTableId().equals(id)) {
                LootPool.Builder pool = LootPool.builder().rolls(new FatLootNumberProvider(new int[]{0, 0, 1, 1})).with(ItemEntry.builder(Mod.ANIMAL_FAT));
                tableBuilder.pool(pool);
            }

        });
    }
}
