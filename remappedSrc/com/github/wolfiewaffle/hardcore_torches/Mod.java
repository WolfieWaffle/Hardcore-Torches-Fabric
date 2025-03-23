package com.github.wolfiewaffle.hardcore_torches;

import com.github.wolfiewaffle.hardcore_torches.block.HardcoreCampfire;
import com.github.wolfiewaffle.hardcore_torches.block.HardcoreFloorTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.block.HardcoreWallTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.block.LanternBlock;
import com.github.wolfiewaffle.hardcore_torches.blockentity.LanternBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.blockentity.HardcoreCampfireBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.blockentity.TorchBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.compat.TrinketsCompat;
import com.github.wolfiewaffle.hardcore_torches.config.HardcoreTorchesConfig;
import com.github.wolfiewaffle.hardcore_torches.item.FireStarterItem;
import com.github.wolfiewaffle.hardcore_torches.item.LanternItem;
import com.github.wolfiewaffle.hardcore_torches.item.OilCanItem;
import com.github.wolfiewaffle.hardcore_torches.item.TorchItem;
import com.github.wolfiewaffle.hardcore_torches.loot.HCTLootTableModifier;
import com.github.wolfiewaffle.hardcore_torches.loot.TorchLootFunction;
import com.github.wolfiewaffle.hardcore_torches.recipe.CampfireRecipe;
import com.github.wolfiewaffle.hardcore_torches.recipe.OilCanRecipe;
import com.github.wolfiewaffle.hardcore_torches.recipe.TorchRecipe;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import com.github.wolfiewaffle.hardcore_torches.util.LanternGroup;
import com.github.wolfiewaffle.hardcore_torches.util.TorchGroup;
import com.github.wolfiewaffle.hardcore_torches.world.ReplaceTorchFeature;
import com.github.wolfiewaffle.hardcore_torches.world.ReplaceTorchFeatureConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;

import java.util.List;

public class Mod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.

	public static HardcoreTorchesConfig config;

	// Tags
	public static final TagKey<Item> TAG_ANIMAL_FAT = TagKey.of(Registries.ITEM.getKey(), new Identifier("minecraft", "animal_fat"));
	public static final TagKey<Item> ALL_TORCH_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "torches"));
	public static final TagKey<Block> FREE_TORCH_LIGHT_BLOCKS = TagKey.of(Registries.BLOCK.getKey(), new Identifier("hardcore_torches", "free_torch_light_blocks"));
	public static final TagKey<Item> FREE_TORCH_LIGHT_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "free_torch_light_items"));
	public static final TagKey<Item> DAMAGE_TORCH_LIGHT_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "damage_torch_light_items"));
	public static final TagKey<Item> CONSUME_TORCH_LIGHT_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "consume_torch_light_items"));
	public static final TagKey<Item> FREE_TORCH_EXTINGUISH_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "free_torch_extinguish_items"));
	public static final TagKey<Item> DAMAGE_TORCH_EXTINGUISH_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "damage_torch_extinguish_items"));
	public static final TagKey<Item> CONSUME_TORCH_EXTINGUISH_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "consume_torch_smother_items"));
	public static final TagKey<Item> FREE_TORCH_SMOTHER_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "free_torch_smother_items"));
	public static final TagKey<Item> DAMAGE_TORCH_SMOTHER_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "damage_torch_smother_items"));
	public static final TagKey<Item> CONSUME_TORCH_SMOTHER_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "consume_torch_smother_items"));
	public static final TagKey<Item> FREE_LANTERN_LIGHT_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "free_lantern_light_items"));
	public static final TagKey<Item> DAMAGE_LANTERN_LIGHT_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "damage_lantern_light_items"));
	public static final TagKey<Item> CONSUME_LANTERN_LIGHT_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier("hardcore_torches", "consume_lantern_light_items"));

	public static final LootFunctionType HARDCORE_TORCH_LOOT_FUNCTION = new LootFunctionType(new TorchLootFunction.Serializer());
	public static final LootFunctionType FUEL_LOOT_FUNCTION = new LootFunctionType(new TorchLootFunction.Serializer());

	public static final Block LIT_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().luminance(state -> 14).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME, ETorchState.LIT, () -> config.defaultTorchFuel);
	public static final Block UNLIT_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), null, ETorchState.UNLIT, () -> config.defaultTorchFuel);
	public static final Block SMOLDERING_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().luminance(state -> 3).sounds(BlockSoundGroup.WOOD), ParticleTypes.SMOKE, ETorchState.SMOLDERING, () -> config.defaultTorchFuel);
	public static final Block BURNT_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), null, ETorchState.BURNT, () -> config.defaultTorchFuel);

	public static final Block LIT_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().luminance(state -> 14).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME, ETorchState.LIT, () -> config.defaultTorchFuel);
	public static final Block UNLIT_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), null, ETorchState.UNLIT, () -> config.defaultTorchFuel);
	public static final Block SMOLDERING_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().luminance(state -> 3).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME, ETorchState.SMOLDERING, () -> config.defaultTorchFuel);
	public static final Block BURNT_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), null, ETorchState.BURNT, () -> config.defaultTorchFuel);

	public static final Block LIT_LANTERN = new LanternBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().luminance(state -> 15).sounds(BlockSoundGroup.LANTERN), true, () -> config.defaultLanternFuel);
	public static final Block UNLIT_LANTERN = new LanternBlock(FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).noCollision().breakInstantly().sounds(BlockSoundGroup.LANTERN), false, () -> config.defaultLanternFuel);

	public static final Block CAMPFIRE_BLOCK = new HardcoreCampfire(true, 1, FabricBlockSettings.create().mapColor(MapColor.BROWN).instrument(Instrument.BASS).strength(2.0F).sounds(BlockSoundGroup.WOOD).luminance(HardcoreCampfire.litBlockEmission(15)).nonOpaque());

	public static final Item OIL_CAN = new OilCanItem(new FabricItemSettings().maxCount(1));
	public static final Item ANIMAL_FAT = new Item(new FabricItemSettings());
	public static final Item FIRE_STARTER = new FireStarterItem(new FabricItemSettings());

	public static final Item CAMPFIRE_ITEM = new BlockItem(CAMPFIRE_BLOCK, new FabricItemSettings());

	public static TorchGroup basicTorches = new TorchGroup("basic");
	public static LanternGroup basicLanterns = new LanternGroup("basic");

	public static BlockEntityType<TorchBlockEntity> TORCH_BLOCK_ENTITY;
	public static BlockEntityType<LanternBlockEntity> LANTERN_BLOCK_ENTITY;
	public static BlockEntityType<HardcoreCampfireBlockEntity> CAMPFIRE_BLOCK_ENTITY;

	// Recipe Types
	public static final RecipeType<OilCanRecipe> OIL_CAN_RECIPE = RecipeType.register("hardcore_torches:oil_can");
	public static final RecipeType<TorchRecipe> TORCH_RECIPE = RecipeType.register("hardcore_torches:torch");
	public static final RecipeType<CampfireRecipe> CAMPFIRE_RECIPE = RecipeType.register("hardcore_torches:campfire");
	public static RecipeSerializer<OilCanRecipe> OIL_RECIPE_SERIALIZER;
	public static RecipeSerializer<TorchRecipe> TORCH_RECIPE_SERIALIZER;
	public static RecipeSerializer<CampfireRecipe> CAMPFIRE_RECIPE_SERIALIZER;

	// World gen
	public static final Identifier REPLACE_FEATURE_ID = new Identifier("hardcore_torches", "replace_block_feature");
	public static final ReplaceTorchFeature REPLACE_FEATURE = new ReplaceTorchFeature(ReplaceTorchFeatureConfig.CODEC);
	public static final ConfiguredFeature<ReplaceTorchFeatureConfig, ReplaceTorchFeature> REPLACE_CONFIGURED_FEATURE = new ConfiguredFeature<>(
			REPLACE_FEATURE,
			new ReplaceTorchFeatureConfig(0, new Identifier("minecraft", "air"))
	);
	public static PlacedFeature REPLACE_FEATURE_PLACED = new PlacedFeature(
			RegistryEntry.of(
					REPLACE_CONFIGURED_FEATURE
			), List.of(CountPlacementModifier.of(1))
	);

	// Mob Drops
	private static final Identifier PANDA_LOOT_TABLE_ID = Identifier.tryParse("minecraft:entities/panda");
	private static final Identifier SNIFFER_LOOT_TABLE_ID = Identifier.tryParse("minecraft:entities/sniffer");
	private static final Identifier CAMEL_LOOT_TABLE_ID = Identifier.tryParse("minecraft:entities/camel");
	private static final Identifier POLAR_BEAR_LOOT_TABLE_ID = Identifier.tryParse("minecraft:entities/polar_bear");
	private static final Identifier PIGLIN_LOOT_TABLE_ID = Identifier.tryParse("minecraft:entities/piglin");
	private static final Identifier PIGLIN_BRUTE_LOOT_TABLE_ID = Identifier.tryParse("minecraft:entities/piglin_brute");
	private static final Identifier HOGLIN_LOOT_TABLE_ID = Identifier.tryParse("minecraft:entities/hoglin");
	private static final Identifier PIG_LOOT_TABLE_ID = Identifier.tryParse("minecraft:entities/pig");
	private static final Identifier COW_LOOT_TABLE_ID = Identifier.tryParse("minecraft:entities/cow");
	private static final Identifier SHEEP_LOOT_TABLE_ID = Identifier.tryParse("minecraft:entities/sheep");
	private static final Identifier LLAMA_LOOT_TABLE_ID = Identifier.tryParse("minecraft:entities/llama");
	private static final Identifier TRADER_LLAMA_LOOT_TABLE_ID = Identifier.tryParse("minecraft:entities/trader_llama");
	private static final Identifier CHICKEN_LOOT_TABLE_ID = Identifier.tryParse("minecraft:entities/chicken");
	private static final Identifier HORSE_LOOT_TABLE_ID = Identifier.tryParse("minecraft:entities/horse");
	private static final Identifier DONKEY_LOOT_TABLE_ID = Identifier.tryParse("minecraft:entities/donkey");
	private static final Identifier MOOSHROOM_LOOT_TABLE_ID = Identifier.tryParse("minecraft:entities/mooshroom");

	@Override
	public void onInitialize() {

		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		basicTorches.add(LIT_TORCH);
		basicTorches.add(UNLIT_TORCH);
		basicTorches.add(SMOLDERING_TORCH);
		basicTorches.add(BURNT_TORCH);
		basicTorches.add(LIT_WALL_TORCH);
		basicTorches.add(UNLIT_WALL_TORCH);
		basicTorches.add(SMOLDERING_WALL_TORCH);
		basicTorches.add(BURNT_WALL_TORCH);

		basicLanterns.add(UNLIT_LANTERN);
		basicLanterns.add(LIT_LANTERN);

		Block[] teTorchBlocks = new Block[] {
				LIT_TORCH,
				UNLIT_TORCH,
				SMOLDERING_TORCH,
				BURNT_TORCH,
				LIT_WALL_TORCH,
				UNLIT_WALL_TORCH,
				SMOLDERING_WALL_TORCH,
				BURNT_WALL_TORCH
		};

		Block[] teLanternBlocks = new Block[] {
				LIT_LANTERN,
				UNLIT_LANTERN
		};

		AutoConfig.register(HardcoreTorchesConfig.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(HardcoreTorchesConfig.class).getConfig();

		Registry.register(Registries.LOOT_FUNCTION_TYPE, "hardcore_torches:torch", HARDCORE_TORCH_LOOT_FUNCTION);
		Registry.register(Registries.LOOT_FUNCTION_TYPE, "hardcore_torches:set_damage", FUEL_LOOT_FUNCTION);

		TORCH_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, "hardcore_torches:torch_block_entity", FabricBlockEntityTypeBuilder.create(TorchBlockEntity::new, teTorchBlocks).build(null));
		LANTERN_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, "hardcore_torches:lantern_entity", FabricBlockEntityTypeBuilder.create(LanternBlockEntity::new, teLanternBlocks).build(null));
		CAMPFIRE_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, "hardcore_torches:campfire_entity", FabricBlockEntityTypeBuilder.create(HardcoreCampfireBlockEntity::new, CAMPFIRE_BLOCK).build(null));

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

		Registry.register(Registries.BLOCK, new Identifier("hardcore_torches", "hardcore_campfire"), CAMPFIRE_BLOCK);

		Item litTorch = Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "lit_torch"), new TorchItem(LIT_TORCH, LIT_WALL_TORCH, new FabricItemSettings(), ETorchState.LIT, config.defaultTorchFuel, basicTorches));
		Item unlitTorch = Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "unlit_torch"), new TorchItem(UNLIT_TORCH, UNLIT_WALL_TORCH, new FabricItemSettings(), ETorchState.UNLIT, config.defaultTorchFuel, basicTorches));
		Item smolderingTorch = Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "smoldering_torch"), new TorchItem(SMOLDERING_TORCH, SMOLDERING_WALL_TORCH, new FabricItemSettings(), ETorchState.SMOLDERING, config.defaultTorchFuel, basicTorches));
		Item burntTorch = Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "burnt_torch"), new TorchItem(BURNT_TORCH, BURNT_WALL_TORCH, new FabricItemSettings(), ETorchState.BURNT, config.defaultTorchFuel, basicTorches));

		Item litLantern = Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "lit_lantern"), new LanternItem(LIT_LANTERN, new FabricItemSettings().maxCount(1), config.defaultLanternFuel, true));
		Item unlitLantern = Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "unlit_lantern"), new LanternItem(UNLIT_LANTERN, new FabricItemSettings().maxCount(1), config.defaultLanternFuel, false));

		Item oilCan = Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "oil_can"), OIL_CAN);
		Item animalFat = Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "animal_fat"), ANIMAL_FAT);
		Item fireStarter = Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "fire_starter"), FIRE_STARTER);

		Item campfire = Registry.register(Registries.ITEM, new Identifier("hardcore_torches", "unlit_campfire"), CAMPFIRE_ITEM);

		// Recipe Types
		OIL_RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier("hardcore_torches", "oil_can"), new OilCanRecipe.Serializer());
		TORCH_RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier("hardcore_torches", "torch"), new TorchRecipe.Serializer());
		CAMPFIRE_RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier("hardcore_torches", "campfire"), new CampfireRecipe.Serializer());

		// Trinkets Compat
		if (FabricLoader.getInstance().isModLoaded("trinkets")) {
			TrinketsCompat.registerTrinkets();
		}

		// Creative Tabs
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(oilCan));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(OilCanItem.setFuel(new ItemStack(oilCan), Mod.config.maxCanFuel)));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(animalFat));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(fireStarter));

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.add(litTorch));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.add(unlitTorch));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.add(smolderingTorch));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.add(burntTorch));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.add(litLantern));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.add(unlitLantern));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.add(campfire));

		Registry.register(Registries.FEATURE, REPLACE_FEATURE_ID, REPLACE_FEATURE);

		// add it to overworld biomes using FAPI
		BiomeModifications.addFeature(
				BiomeSelectors.all(),
				// the feature is to be added while flowers and trees are being generated
				GenerationStep.Feature.TOP_LAYER_MODIFICATION,
				RegistryKey.of(RegistryKeys.PLACED_FEATURE, REPLACE_FEATURE_ID));

		// Loot Tables
		HCTLootTableModifier.registerLootPools();
	}
}
