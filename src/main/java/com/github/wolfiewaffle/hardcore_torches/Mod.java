//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches;

import com.github.wolfiewaffle.hardcore_torches.block.HardcoreFloorTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.block.HardcoreWallTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.block.LanternBlock;
import com.github.wolfiewaffle.hardcore_torches.blockentity.LanternBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.blockentity.TorchBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.compat.TrinketsCompat;
import com.github.wolfiewaffle.hardcore_torches.config.HardcoreTorchesConfig;
import com.github.wolfiewaffle.hardcore_torches.item.FireStarterItem;
import com.github.wolfiewaffle.hardcore_torches.item.LanternItem;
import com.github.wolfiewaffle.hardcore_torches.item.OilCanItem;
import com.github.wolfiewaffle.hardcore_torches.item.TorchItem;
import com.github.wolfiewaffle.hardcore_torches.loot.HCTLootNumberProviderTypes;
import com.github.wolfiewaffle.hardcore_torches.loot.HCTLootTableModifier;
import com.github.wolfiewaffle.hardcore_torches.loot.LanternLootFunction;
import com.github.wolfiewaffle.hardcore_torches.loot.TorchLootFunction;
import com.github.wolfiewaffle.hardcore_torches.recipe.OilCanRecipe;
import com.github.wolfiewaffle.hardcore_torches.recipe.TorchRecipe;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import com.github.wolfiewaffle.hardcore_torches.util.TorchGroup;
import com.github.wolfiewaffle.hardcore_torches.world.ReplaceTorchFeature;
import com.github.wolfiewaffle.hardcore_torches.world.ReplaceTorchFeatureConfig;
import com.mojang.datafixers.types.Type;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep.Feature;

public class Mod implements ModInitializer {
  public static HardcoreTorchesConfig config;
  public static final TagKey<Item> TAG_ANIMAL_FAT;
  public static final TagKey<Item> ALL_TORCH_ITEMS;
  public static final TagKey<Block> FREE_TORCH_LIGHT_BLOCKS;
  public static final TagKey<Item> FREE_TORCH_LIGHT_ITEMS;
  public static final TagKey<Item> DAMAGE_TORCH_LIGHT_ITEMS;
  public static final TagKey<Item> CONSUME_TORCH_LIGHT_ITEMS;
  public static final TagKey<Item> FREE_TORCH_EXTINGUISH_ITEMS;
  public static final TagKey<Item> DAMAGE_TORCH_EXTINGUISH_ITEMS;
  public static final TagKey<Item> CONSUME_TORCH_EXTINGUISH_ITEMS;
  public static final TagKey<Item> FREE_TORCH_SMOTHER_ITEMS;
  public static final TagKey<Item> DAMAGE_TORCH_SMOTHER_ITEMS;
  public static final TagKey<Item> CONSUME_TORCH_SMOTHER_ITEMS;
  public static final TagKey<Item> FREE_LANTERN_LIGHT_ITEMS;
  public static final TagKey<Item> DAMAGE_LANTERN_LIGHT_ITEMS;
  public static final TagKey<Item> CONSUME_LANTERN_LIGHT_ITEMS;
  public static final LootFunctionType HARDCORE_TORCH_LOOT_FUNCTION;
  public static final LootFunctionType FUEL_LOOT_FUNCTION;
  public static final Block LIT_TORCH;
  public static final Block UNLIT_TORCH;
  public static final Block SMOLDERING_TORCH;
  public static final Block BURNT_TORCH;
  public static final Block LIT_WALL_TORCH;
  public static final Block UNLIT_WALL_TORCH;
  public static final Block SMOLDERING_WALL_TORCH;
  public static final Block BURNT_WALL_TORCH;
  public static final Block LIT_LANTERN;
  public static final Block UNLIT_LANTERN;
  public static final Item OIL_CAN;
  public static final Item ANIMAL_FAT;
  public static final Item FIRE_STARTER;
  public static TorchGroup basicTorches;
  public static BlockEntityType<TorchBlockEntity> TORCH_BLOCK_ENTITY;
  public static BlockEntityType<LanternBlockEntity> LANTERN_BLOCK_ENTITY;
  public static final RecipeType<OilCanRecipe> OIL_CAN_RECIPE;
  public static final RecipeType<TorchRecipe> TORCH_RECIPE;
  public static RecipeSerializer<OilCanRecipe> OIL_RECIPE_SERIALIZER;
  public static RecipeSerializer<TorchRecipe> TORCH_RECIPE_SERIALIZER;
  public static final Identifier REPLACE_FEATURE_ID;
  public static final ReplaceTorchFeature REPLACE_FEATURE;

  public Mod() {
  }

  public void onInitialize() {
    HCTLootNumberProviderTypes.loadThisClass();
    basicTorches.add(LIT_TORCH);
    basicTorches.add(UNLIT_TORCH);
    basicTorches.add(SMOLDERING_TORCH);
    basicTorches.add(BURNT_TORCH);
    basicTorches.add(LIT_WALL_TORCH);
    basicTorches.add(UNLIT_WALL_TORCH);
    basicTorches.add(SMOLDERING_WALL_TORCH);
    basicTorches.add(BURNT_WALL_TORCH);
    Block[] teTorchBlocks = new Block[]{LIT_TORCH, UNLIT_TORCH, SMOLDERING_TORCH, BURNT_TORCH, LIT_WALL_TORCH, UNLIT_WALL_TORCH, SMOLDERING_WALL_TORCH, BURNT_WALL_TORCH};
    Block[] teLanternBlocks = new Block[]{LIT_LANTERN, UNLIT_LANTERN};
    AutoConfig.register(HardcoreTorchesConfig.class, JanksonConfigSerializer::new);
    config = (HardcoreTorchesConfig)AutoConfig.getConfigHolder(HardcoreTorchesConfig.class).getConfig();
    Registry.register(Registries.LOOT_FUNCTION_TYPE, "hardcore_torches:torch", HARDCORE_TORCH_LOOT_FUNCTION);
    Registry.register(Registries.LOOT_FUNCTION_TYPE, "hardcore_torches:set_damage", FUEL_LOOT_FUNCTION);
    TORCH_BLOCK_ENTITY = (BlockEntityType)Registry.register(Registries.BLOCK_ENTITY_TYPE, "hardcore_torches:torch_block_entity", FabricBlockEntityTypeBuilder.create(TorchBlockEntity::new, teTorchBlocks).build((Type)null));
    LANTERN_BLOCK_ENTITY = (BlockEntityType)Registry.register(Registries.BLOCK_ENTITY_TYPE, "hardcore_torches:lantern_entity", FabricBlockEntityTypeBuilder.create(LanternBlockEntity::new, teLanternBlocks).build((Type)null));
    Registry.register(Registries.BLOCK, new Identifier("hardcore_torches", "lit_torch"), LIT_TORCH);
    Registry.register(Registries.BLOCK, new Identifier("hardcore_torches", "unlit_torch"), UNLIT_TORCH);
    Registry.register(Registries.BLOCK, new Identifier("hardcore_torches", "smoldering_torch"), SMOLDERING_TORCH);
    Registry.register(Registries.BLOCK, new Identifier("hardcore_torches", "burnt_torch"), BURNT_TORCH);
    Registry.register(Registries.BLOCK, new Identifier("hardcore_torches", "lit_wall_torch"), LIT_WALL_TORCH);
    Registry.register(Registries.BLOCK, new Identifier("hardcore_torches", "unlit_wall_torch"), UNLIT_WALL_TORCH);
    Registry.register(Registries.BLOCK, new Identifier("hardcore_torches", "smoldering_wall_torch"), SMOLDERING_WALL_TORCH);
    Registry.register(Registries.BLOCK, new Identifier("hardcore_torches", "burnt_wall_torch"), BURNT_WALL_TORCH);
    Registry.register(Registries.BLOCK, new Identifier("hardcore_torches", "lit_lantern"), LIT_LANTERN);
    Registry.register(Registries.BLOCK, new Identifier("hardcore_torches", "unlit_lantern"), UNLIT_LANTERN);
    Item litTorch = (Item)Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "lit_torch"), new TorchItem(LIT_TORCH, LIT_WALL_TORCH, new FabricItemSettings(), ETorchState.LIT, config.defaultTorchFuel, basicTorches));
    Item unlitTorch = (Item)Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "unlit_torch"), new TorchItem(UNLIT_TORCH, UNLIT_WALL_TORCH, new FabricItemSettings(), ETorchState.UNLIT, config.defaultTorchFuel, basicTorches));
    Item smolderingTorch = (Item)Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "smoldering_torch"), new TorchItem(SMOLDERING_TORCH, SMOLDERING_WALL_TORCH, new FabricItemSettings(), ETorchState.SMOLDERING, config.defaultTorchFuel, basicTorches));
    Item burntTorch = (Item)Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "burnt_torch"), new TorchItem(BURNT_TORCH, BURNT_WALL_TORCH, new FabricItemSettings(), ETorchState.BURNT, config.defaultTorchFuel, basicTorches));
    Item litLantern = (Item)Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "lit_lantern"), new LanternItem(LIT_LANTERN, (new FabricItemSettings()).maxCount(1), config.defaultLanternFuel, true));
    Item unlitLantern = (Item)Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "unlit_lantern"), new LanternItem(UNLIT_LANTERN, (new FabricItemSettings()).maxCount(1), config.defaultLanternFuel, false));
    Item oilCan = (Item)Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "oil_can"), OIL_CAN);
    Item animalFat = (Item)Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "animal_fat"), ANIMAL_FAT);
    Item fireStarter = (Item)Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "fire_starter"), FIRE_STARTER);
    OIL_RECIPE_SERIALIZER = (RecipeSerializer)Registry.register(Registries.RECIPE_SERIALIZER, new Identifier("hardcore_torches", "oil_can"), new OilCanRecipe.Serializer());
    TORCH_RECIPE_SERIALIZER = (RecipeSerializer)Registry.register(Registries.RECIPE_SERIALIZER, new Identifier("hardcore_torches", "torch"), new TorchRecipe.Serializer());
    HCTLootTableModifier.registerLootPools();
    if (FabricLoader.getInstance().isModLoaded("trinkets")) {
      TrinketsCompat.registerTrinkets();
    }

    ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((ItemGroupEvents.ModifyEntries)(entries) -> entries.add(animalFat));
    ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((ItemGroupEvents.ModifyEntries)(entries) -> entries.add(fireStarter));
    ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((ItemGroupEvents.ModifyEntries)(entries) -> entries.add(oilCan));
    ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((ItemGroupEvents.ModifyEntries)(entries) -> entries.add(OilCanItem.setFuel(new ItemStack(oilCan), config.maxCanFuel)));
    ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register((ItemGroupEvents.ModifyEntries)(entries) -> entries.add(litTorch));
    ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register((ItemGroupEvents.ModifyEntries)(entries) -> entries.add(unlitTorch));
    ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register((ItemGroupEvents.ModifyEntries)(entries) -> entries.add(smolderingTorch));
    ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register((ItemGroupEvents.ModifyEntries)(entries) -> entries.add(burntTorch));
    ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register((ItemGroupEvents.ModifyEntries)(entries) -> entries.add(litLantern));
    ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register((ItemGroupEvents.ModifyEntries)(entries) -> entries.add(unlitLantern));
    Registry.register(Registries.FEATURE, REPLACE_FEATURE_ID, REPLACE_FEATURE);
    BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), Feature.TOP_LAYER_MODIFICATION, RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("hardcore_torches", "replace_block_feature")));
  }

  static {
    TAG_ANIMAL_FAT = TagKey.of(Registries.ITEM.getKey(), new Identifier("minecraft", "animal_fat"));
    ALL_TORCH_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "torches"));
    FREE_TORCH_LIGHT_BLOCKS = TagKey.of(Registries.BLOCK.getKey(), new Identifier("hardcore_torches", "free_torch_light_blocks"));
    FREE_TORCH_LIGHT_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "free_torch_light_items"));
    DAMAGE_TORCH_LIGHT_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "damage_torch_light_items"));
    CONSUME_TORCH_LIGHT_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "consume_torch_light_items"));
    FREE_TORCH_EXTINGUISH_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "free_torch_extinguish_items"));
    DAMAGE_TORCH_EXTINGUISH_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "damage_torch_extinguish_items"));
    CONSUME_TORCH_EXTINGUISH_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "consume_torch_smother_items"));
    FREE_TORCH_SMOTHER_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "free_torch_smother_items"));
    DAMAGE_TORCH_SMOTHER_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "damage_torch_smother_items"));
    CONSUME_TORCH_SMOTHER_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "consume_torch_smother_items"));
    FREE_LANTERN_LIGHT_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "free_lantern_light_items"));
    DAMAGE_LANTERN_LIGHT_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "damage_lantern_light_items"));
    CONSUME_LANTERN_LIGHT_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "consume_lantern_light_items"));
    HARDCORE_TORCH_LOOT_FUNCTION = new LootFunctionType(new TorchLootFunction.Serializer());
    FUEL_LOOT_FUNCTION = new LootFunctionType(new LanternLootFunction.Serializer());
    LIT_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().luminance((state) -> 14).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME, ETorchState.LIT, () -> config.defaultTorchFuel);
    UNLIT_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), (ParticleEffect)null, ETorchState.UNLIT, () -> config.defaultTorchFuel);
    SMOLDERING_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().luminance((state) -> 3).sounds(BlockSoundGroup.WOOD), ParticleTypes.SMOKE, ETorchState.SMOLDERING, () -> config.defaultTorchFuel);
    BURNT_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), (ParticleEffect)null, ETorchState.BURNT, () -> config.defaultTorchFuel);
    LIT_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().luminance((state) -> 14).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME, ETorchState.LIT, () -> config.defaultTorchFuel);
    UNLIT_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), (ParticleEffect)null, ETorchState.UNLIT, () -> config.defaultTorchFuel);
    SMOLDERING_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().luminance((state) -> 3).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME, ETorchState.SMOLDERING, () -> config.defaultTorchFuel);
    BURNT_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), (ParticleEffect)null, ETorchState.BURNT, () -> config.defaultTorchFuel);
    LIT_LANTERN = new LanternBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().luminance((state) -> 15).sounds(BlockSoundGroup.LANTERN), true, () -> config.defaultLanternFuel);
    UNLIT_LANTERN = new LanternBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().sounds(BlockSoundGroup.LANTERN), false, () -> config.defaultLanternFuel);
    OIL_CAN = new OilCanItem((new FabricItemSettings()).maxCount(1));
    ANIMAL_FAT = new Item(new FabricItemSettings());
    FIRE_STARTER = new FireStarterItem(new FabricItemSettings());
    basicTorches = new TorchGroup("basic");
    OIL_CAN_RECIPE = RecipeType.register("hardcore_torches:oil_can");
    TORCH_RECIPE = RecipeType.register("hardcore_torches:torch");
    REPLACE_FEATURE_ID = new Identifier("hardcore_torches", "replace_block_feature");
    REPLACE_FEATURE = new ReplaceTorchFeature(ReplaceTorchFeatureConfig.CODEC);
  }
}
