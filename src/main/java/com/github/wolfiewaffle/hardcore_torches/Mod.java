package com.github.wolfiewaffle.hardcore_torches;

import com.github.wolfiewaffle.hardcore_torches.block.HardcoreFloorTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.block.HardcoreWallTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.block.LanternBlock;
import com.github.wolfiewaffle.hardcore_torches.blockentity.LanternBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.blockentity.TorchBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.config.HardcoreTorchesConfig;
import com.github.wolfiewaffle.hardcore_torches.item.FireStarterItem;
import com.github.wolfiewaffle.hardcore_torches.item.LanternItem;
import com.github.wolfiewaffle.hardcore_torches.item.OilCanItem;
import com.github.wolfiewaffle.hardcore_torches.item.TorchItem;
import com.github.wolfiewaffle.hardcore_torches.loot.HCTLootTableModifier;
import com.github.wolfiewaffle.hardcore_torches.loot.LanternLootFunction;
import com.github.wolfiewaffle.hardcore_torches.loot.TorchLootFunction;
import com.github.wolfiewaffle.hardcore_torches.recipe.OilCanRecipe;
import com.github.wolfiewaffle.hardcore_torches.recipe.TorchRecipe;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import com.github.wolfiewaffle.hardcore_torches.util.TorchGroup;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Mod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.

	public static HardcoreTorchesConfig config;

	// Tags
	public static final TagKey<Item> TAG_ANIMAL_FAT = TagKey.of(Registry.ITEM_KEY, new Identifier("minecraft", "animal_fat"));
	public static final TagKey<Item> ALL_TORCH_ITEMS = TagKey.of(Registry.ITEM_KEY, new Identifier("hardcore_torches", "torches"));
	public static final TagKey<Block> FREE_TORCH_LIGHT_BLOCKS = TagKey.of(Registry.BLOCK_KEY, new Identifier("hardcore_torches", "free_torch_light_blocks"));
	public static final TagKey<Item> FREE_TORCH_LIGHT_ITEMS = TagKey.of(Registry.ITEM_KEY, new Identifier("hardcore_torches", "free_torch_light_items"));
	public static final TagKey<Item> DAMAGE_TORCH_LIGHT_ITEMS = TagKey.of(Registry.ITEM_KEY, new Identifier("hardcore_torches", "damage_torch_light_items"));
	public static final TagKey<Item> CONSUME_TORCH_LIGHT_ITEMS = TagKey.of(Registry.ITEM_KEY, new Identifier("hardcore_torches", "consume_torch_light_items"));
	public static final TagKey<Item> FREE_TORCH_EXTINGUISH_ITEMS = TagKey.of(Registry.ITEM_KEY, new Identifier("hardcore_torches", "free_torch_extinguish_items"));
	public static final TagKey<Item> DAMAGE_TORCH_EXTINGUISH_ITEMS = TagKey.of(Registry.ITEM_KEY, new Identifier("hardcore_torches", "damage_torch_extinguish_items"));
	public static final TagKey<Item> CONSUME_TORCH_EXTINGUISH_ITEMS = TagKey.of(Registry.ITEM_KEY, new Identifier("hardcore_torches", "consume_torch_smother_items"));
	public static final TagKey<Item> FREE_TORCH_SMOTHER_ITEMS = TagKey.of(Registry.ITEM_KEY, new Identifier("hardcore_torches", "free_torch_smother_items"));
	public static final TagKey<Item> DAMAGE_TORCH_SMOTHER_ITEMS = TagKey.of(Registry.ITEM_KEY, new Identifier("hardcore_torches", "damage_torch_smother_items"));
	public static final TagKey<Item> CONSUME_TORCH_SMOTHER_ITEMS = TagKey.of(Registry.ITEM_KEY, new Identifier("hardcore_torches", "consume_torch_smother_items"));
	public static final TagKey<Item> FREE_LANTERN_LIGHT_ITEMS = TagKey.of(Registry.ITEM_KEY, new Identifier("hardcore_torches", "free_lantern_light_items"));
	public static final TagKey<Item> DAMAGE_LANTERN_LIGHT_ITEMS = TagKey.of(Registry.ITEM_KEY, new Identifier("hardcore_torches", "damage_lantern_light_items"));
	public static final TagKey<Item> CONSUME_LANTERN_LIGHT_ITEMS = TagKey.of(Registry.ITEM_KEY, new Identifier("hardcore_torches", "consume_lantern_light_items"));

	public static final LootFunctionType HARDCORE_TORCH_LOOT_FUNCTION = new LootFunctionType(new TorchLootFunction.Serializer());
	public static final LootFunctionType FUEL_LOOT_FUNCTION = new LootFunctionType(new LanternLootFunction.Serializer());

	public static final Block LIT_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().luminance(state -> 14).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME, ETorchState.LIT, () -> config.defaultTorchFuel);
	public static final Block UNLIT_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), null, ETorchState.UNLIT, () -> config.defaultTorchFuel);
	public static final Block SMOLDERING_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().luminance(state -> 3).sounds(BlockSoundGroup.WOOD), ParticleTypes.SMOKE, ETorchState.SMOLDERING, () -> config.defaultTorchFuel);
	public static final Block BURNT_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), null, ETorchState.BURNT, () -> config.defaultTorchFuel);

	public static final Block LIT_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().luminance(state -> 14).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME, ETorchState.LIT, () -> config.defaultTorchFuel);
	public static final Block UNLIT_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), null, ETorchState.UNLIT, () -> config.defaultTorchFuel);
	public static final Block SMOLDERING_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().luminance(state -> 3).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME, ETorchState.SMOLDERING, () -> config.defaultTorchFuel);
	public static final Block BURNT_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), null, ETorchState.BURNT, () -> config.defaultTorchFuel);

	public static final Block LIT_LANTERN = new LanternBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().luminance(state -> 15).sounds(BlockSoundGroup.LANTERN), true, () -> config.defaultLanternFuel);
	public static final Block UNLIT_LANTERN = new LanternBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().sounds(BlockSoundGroup.LANTERN), false, () -> config.defaultLanternFuel);

	public static final Item OIL_CAN = new OilCanItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1));
	public static final Item ANIMAL_FAT = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item FIRE_STARTER = new FireStarterItem(new FabricItemSettings().group(ItemGroup.TOOLS));

	public static TorchGroup basicTorches = new TorchGroup("basic");

	public static BlockEntityType<TorchBlockEntity> TORCH_BLOCK_ENTITY;
	public static BlockEntityType<LanternBlockEntity> LANTERN_BLOCK_ENTITY;

	// Recipe Types
	public static final RecipeType<OilCanRecipe> OIL_CAN_RECIPE = RecipeType.register("hardcore_torches:oil_can");
	public static final RecipeType<TorchRecipe> TORCH_RECIPE = RecipeType.register("hardcore_torches:torch");
	public static RecipeSerializer<OilCanRecipe> OIL_RECIPE_SERIALIZER;
	public static RecipeSerializer<TorchRecipe> TORCH_RECIPE_SERIALIZER;

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

		Registry.register(Registry.LOOT_FUNCTION_TYPE, "hardcore_torches:torch", HARDCORE_TORCH_LOOT_FUNCTION);
		Registry.register(Registry.LOOT_FUNCTION_TYPE, "hardcore_torches:set_damage", FUEL_LOOT_FUNCTION);

		TORCH_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "hardcore_torches:torch_block_entity", FabricBlockEntityTypeBuilder.create(TorchBlockEntity::new, teTorchBlocks).build(null));
		LANTERN_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "hardcore_torches:lantern_entity", FabricBlockEntityTypeBuilder.create(LanternBlockEntity::new, teLanternBlocks).build(null));

		Registry.register(Registry.BLOCK, new Identifier("hardcore_torches", "lit_torch"), LIT_TORCH);
		Registry.register(Registry.BLOCK, new Identifier("hardcore_torches", "unlit_torch"), UNLIT_TORCH);
		Registry.register(Registry.BLOCK, new Identifier("hardcore_torches", "smoldering_torch"), SMOLDERING_TORCH);
		Registry.register(Registry.BLOCK, new Identifier("hardcore_torches", "burnt_torch"), BURNT_TORCH);

		Registry.register(Registry.BLOCK, new Identifier("hardcore_torches", "lit_wall_torch"), LIT_WALL_TORCH);
		Registry.register(Registry.BLOCK, new Identifier("hardcore_torches", "unlit_wall_torch"), UNLIT_WALL_TORCH);
		Registry.register(Registry.BLOCK, new Identifier("hardcore_torches", "smoldering_wall_torch"), SMOLDERING_WALL_TORCH);
		Registry.register(Registry.BLOCK, new Identifier("hardcore_torches", "burnt_wall_torch"), BURNT_WALL_TORCH);

		Registry.register(Registry.BLOCK, new Identifier("hardcore_torches", "lit_lantern"), LIT_LANTERN);
		Registry.register(Registry.BLOCK, new Identifier("hardcore_torches", "unlit_lantern"), UNLIT_LANTERN);

		Registry.register(Registry.ITEM, new Identifier("hardcore_torches", "lit_torch"), new TorchItem(LIT_TORCH, LIT_WALL_TORCH, new FabricItemSettings().group(ItemGroup.DECORATIONS), ETorchState.LIT, config.defaultTorchFuel, basicTorches));
		Registry.register(Registry.ITEM, new Identifier("hardcore_torches", "unlit_torch"), new TorchItem(UNLIT_TORCH, UNLIT_WALL_TORCH, new FabricItemSettings().group(ItemGroup.DECORATIONS), ETorchState.UNLIT, config.defaultTorchFuel, basicTorches));
		Registry.register(Registry.ITEM, new Identifier("hardcore_torches", "smoldering_torch"), new TorchItem(SMOLDERING_TORCH, SMOLDERING_WALL_TORCH, new FabricItemSettings().group(ItemGroup.DECORATIONS), ETorchState.SMOLDERING, config.defaultTorchFuel, basicTorches));
		Registry.register(Registry.ITEM, new Identifier("hardcore_torches", "burnt_torch"), new TorchItem(BURNT_TORCH, BURNT_WALL_TORCH, new FabricItemSettings().group(ItemGroup.DECORATIONS), ETorchState.BURNT, config.defaultTorchFuel, basicTorches));

		Registry.register(Registry.ITEM, new Identifier("hardcore_torches", "lit_lantern"), new LanternItem(LIT_LANTERN, new FabricItemSettings().group(ItemGroup.DECORATIONS).maxCount(1), config.defaultLanternFuel, true));
		Registry.register(Registry.ITEM, new Identifier("hardcore_torches", "unlit_lantern"), new LanternItem(UNLIT_LANTERN, new FabricItemSettings().group(ItemGroup.DECORATIONS).maxCount(1), config.defaultLanternFuel, false));

		Registry.register(Registry.ITEM, new Identifier("hardcore_torches", "oil_can"), OIL_CAN);
		Registry.register(Registry.ITEM, new Identifier("hardcore_torches", "animal_fat"), ANIMAL_FAT);
		Registry.register(Registry.ITEM, new Identifier("hardcore_torches", "fire_starter"), FIRE_STARTER);

		// Recipe Types
		OIL_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier("hardcore_torches", "oil_can"), new OilCanRecipe.Serializer());
		TORCH_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier("hardcore_torches", "torch"), new TorchRecipe.Serializer());

		// Loot Tables
		HCTLootTableModifier.registerLootPools();


	}
}
