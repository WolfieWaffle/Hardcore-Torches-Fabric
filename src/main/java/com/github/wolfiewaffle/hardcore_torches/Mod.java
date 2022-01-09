package com.github.wolfiewaffle.hardcore_torches;

import com.github.wolfiewaffle.hardcore_torches.block.HardcoreFloorTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.block.HardcoreWallTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.block.LanternBlock;
import com.github.wolfiewaffle.hardcore_torches.blockentity.LanternBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.blockentity.TorchBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.config.HardcoreTorchesConfig;
import com.github.wolfiewaffle.hardcore_torches.item.LanternItem;
import com.github.wolfiewaffle.hardcore_torches.item.OilCanItem;
import com.github.wolfiewaffle.hardcore_torches.item.TorchItem;
import com.github.wolfiewaffle.hardcore_torches.loot.LanternLootFunction;
import com.github.wolfiewaffle.hardcore_torches.loot.TorchLootFunction;
import com.github.wolfiewaffle.hardcore_torches.recipe.Recipes;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import com.github.wolfiewaffle.hardcore_torches.util.TorchGroup;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger("hardcore_torches");

	// Tags
	public static final Tag<Item> ALL_TORCH_ITEMS = TagFactory.ITEM.create(new Identifier("hardcore_torches", "torches"));
	public static final Tag<Block> FREE_TORCH_LIGHT_BLOCKS = TagFactory.BLOCK.create(new Identifier("hardcore_torches", "free_torch_light_blocks"));
	public static final Tag<Item> FREE_TORCH_LIGHT_ITEMS = TagFactory.ITEM.create(new Identifier("hardcore_torches", "free_torch_light_items"));
	public static final Tag<Item> DAMAGE_TORCH_LIGHT_ITEMS = TagFactory.ITEM.create(new Identifier("hardcore_torches", "damage_torch_light_items"));
	public static final Tag<Item> CONSUME_TORCH_LIGHT_ITEMS = TagFactory.ITEM.create(new Identifier("hardcore_torches", "consume_torch_light_items"));
	public static final Tag<Item> FREE_TORCH_EXTINGUISH_ITEMS = TagFactory.ITEM.create(new Identifier("hardcore_torches", "free_torch_extinguish_items"));
	public static final Tag<Item> DAMAGE_TORCH_EXTINGUISH_ITEMS = TagFactory.ITEM.create(new Identifier("hardcore_torches", "damage_torch_extinguish_items"));
	public static final Tag<Item> CONSUME_TORCH_EXTINGUISH_ITEMS = TagFactory.ITEM.create(new Identifier("hardcore_torches", "consume_torch_extinguish_items"));
	public static final Tag<Item> FREE_TORCH_SMOTHER_ITEMS = TagFactory.ITEM.create(new Identifier("hardcore_torches", "free_torch_smother_items"));
	public static final Tag<Item> DAMAGE_TORCH_SMOTHER_ITEMS = TagFactory.ITEM.create(new Identifier("hardcore_torches", "damage_torch_smother_items"));
	public static final Tag<Item> CONSUME_TORCH_SMOTHER_ITEMS = TagFactory.ITEM.create(new Identifier("hardcore_torches", "consume_torch_smother_items"));
	public static final Tag<Item> FREE_LANTERN_LIGHT_ITEMS = TagFactory.ITEM.create(new Identifier("hardcore_torches", "free_lantern_light_items"));
	public static final Tag<Item> DAMAGE_LANTERN_LIGHT_ITEMS = TagFactory.ITEM.create(new Identifier("hardcore_torches", "damage_lantern_light_items"));
	public static final Tag<Item> CONSUME_LANTERN_LIGHT_ITEMS = TagFactory.ITEM.create(new Identifier("hardcore_torches", "consume_lantern_light_items"));

	public static final LootFunctionType HARDCORE_TORCH_LOOT_FUNCTION = new LootFunctionType(new TorchLootFunction.Serializer());
	public static final LootFunctionType FUEL_LOOT_FUNCTION = new LootFunctionType(new LanternLootFunction.Serializer());

	public static final Block LIT_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().luminance(state -> 14).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME, ETorchState.LIT);
	public static final Block UNLIT_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), null, ETorchState.UNLIT);
	public static final Block SMOLDERING_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().luminance(state -> 3).sounds(BlockSoundGroup.WOOD), ParticleTypes.SMOKE, ETorchState.SMOLDERING);
	public static final Block BURNT_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), null, ETorchState.BURNT);

	public static final Block LIT_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().luminance(state -> 14).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME, ETorchState.LIT);
	public static final Block UNLIT_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), null, ETorchState.UNLIT);
	public static final Block SMOLDERING_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().luminance(state -> 3).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME, ETorchState.SMOLDERING);
	public static final Block BURNT_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), null, ETorchState.BURNT);

	public static final Block LIT_LANTERN = new LanternBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().luminance(state -> 15).sounds(BlockSoundGroup.LANTERN), true);
	public static final Block UNLIT_LANTERN = new LanternBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().sounds(BlockSoundGroup.LANTERN), false);

	public static final Item OIL_CAN = new OilCanItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1));

	public static TorchGroup basicTorches = new TorchGroup("basic");

	public static BlockEntityType<TorchBlockEntity> TORCH_BLOCK_ENTITY;
	public static BlockEntityType<LanternBlockEntity> LANTERN_BLOCK_ENTITY;

	public static HardcoreTorchesConfig config;

	// Recipes
	public static JsonObject TORCH_RECIPE = null;
	public static JsonObject VANILLA_TORCH_RECIPE = null;
	public static JsonObject LANTERN_RECIPE = null;


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

		TORCH_RECIPE = Recipes.createShapedRecipeJson(
				Lists.newArrayList(
						'#',
						'|'
				), //The keys we are using for the input items/tags.
				Lists.newArrayList(new Identifier("minecraft", "coals"), new Identifier("stick")), //The items/tags we are using as input.
				Lists.newArrayList("tag", "item"), //Whether the input we provided is a tag or an item.
				Lists.newArrayList(
						"#",
						"|"
				), //The crafting pattern.
				(config.craftUnlit) ? new Identifier("hardcore_torches:unlit_torch") : new Identifier("hardcore_torches:lit_torch"), //The crafting output
				config.craftAmount
			);

		VANILLA_TORCH_RECIPE = Recipes.createShapedRecipeJson(
				Lists.newArrayList(
						'#',
						'|'
				), //The keys we are using for the input items/tags.
				Lists.newArrayList(new Identifier("glowstone_dust"), new Identifier("stick")), //The items/tags we are using as input.
				Lists.newArrayList("item", "item"), //Whether the input we provided is a tag or an item.
				Lists.newArrayList(
						"#",
						"|"
				), //The crafting pattern.
				new Identifier("minecraft:torch"), //The crafting output
				1
			);

		LANTERN_RECIPE = Recipes.createShapedRecipeJson(
				Lists.newArrayList(
						'#',
						'|'
				), //The keys we are using for the input items/tags.
				Lists.newArrayList(new Identifier("iron_nugget"), new Identifier("hardcore_torches", "torches")), //The items/tags we are using as input.
				Lists.newArrayList("item", "tag"), //Whether the input we provided is a tag or an item.
				Lists.newArrayList(
						"###",
						"#|#",
						"###"
				), //The crafting pattern.
				new Identifier("hardcore_torches:unlit_lantern"), //The crafting output
				1
			);
	}
}
