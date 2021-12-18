package com.github.wolfiewaffle.hardcore_torches;

import com.github.wolfiewaffle.hardcore_torches.block.HardcoreFloorTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.block.HardcoreWallTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.blockentity.TorchBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.config.HardcoreTorchesConfig;
import com.github.wolfiewaffle.hardcore_torches.item.TorchItem;
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
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger("hardcore_torches");

	public static final LootFunctionType HARDCORE_TORCH_LOOT_FUNCTION = new LootFunctionType(new TorchLootFunction.Serializer());

	public static final Block LIT_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().luminance(state -> 14).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME, ETorchState.LIT);
	public static final Block UNLIT_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), null, ETorchState.UNLIT);
	public static final Block SMOLDERING_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().luminance(state -> 3).sounds(BlockSoundGroup.WOOD), ParticleTypes.SMOKE, ETorchState.SMOLDERING);
	public static final Block BURNT_TORCH = new HardcoreFloorTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), null, ETorchState.BURNT);

	public static final Block LIT_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().luminance(state -> 14).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME, ETorchState.LIT);
	public static final Block UNLIT_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), null, ETorchState.UNLIT);
	public static final Block SMOLDERING_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().luminance(state -> 3).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME, ETorchState.SMOLDERING);
	public static final Block BURNT_WALL_TORCH = new HardcoreWallTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD), null, ETorchState.BURNT);

	public static TorchGroup basicTorches = new TorchGroup();

	public static BlockEntityType<TorchBlockEntity> TORCH_BLOCK_ENTITY;

	public static HardcoreTorchesConfig config;

	public static JsonObject TORCH_RECIPE = null;


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

		Block[] teTorches = new Block[] {
				LIT_TORCH,
				UNLIT_TORCH,
				SMOLDERING_TORCH,
				BURNT_TORCH,
				LIT_WALL_TORCH,
				UNLIT_WALL_TORCH,
				SMOLDERING_WALL_TORCH,
				BURNT_WALL_TORCH
		};

		AutoConfig.register(HardcoreTorchesConfig.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(HardcoreTorchesConfig.class).getConfig();

		Registry.register(Registry.LOOT_FUNCTION_TYPE, "hardcore_torches:set_damage", HARDCORE_TORCH_LOOT_FUNCTION);

		TORCH_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "hardcore_torches:torch_block_entity", FabricBlockEntityTypeBuilder.create(TorchBlockEntity::new, teTorches).build(null));

		Registry.register(Registry.BLOCK, new Identifier("hardcore_torches", "lit_torch"), LIT_TORCH);
		Registry.register(Registry.BLOCK, new Identifier("hardcore_torches", "unlit_torch"), UNLIT_TORCH);
		Registry.register(Registry.BLOCK, new Identifier("hardcore_torches", "smoldering_torch"), SMOLDERING_TORCH);
		Registry.register(Registry.BLOCK, new Identifier("hardcore_torches", "burnt_torch"), BURNT_TORCH);

		Registry.register(Registry.BLOCK, new Identifier("hardcore_torches", "lit_wall_torch"), LIT_WALL_TORCH);
		Registry.register(Registry.BLOCK, new Identifier("hardcore_torches", "unlit_wall_torch"), UNLIT_WALL_TORCH);
		Registry.register(Registry.BLOCK, new Identifier("hardcore_torches", "smoldering_wall_torch"), SMOLDERING_WALL_TORCH);
		Registry.register(Registry.BLOCK, new Identifier("hardcore_torches", "burnt_wall_torch"), BURNT_WALL_TORCH);

		Registry.register(Registry.ITEM, new Identifier("hardcore_torches", "lit_torch"), new TorchItem(LIT_TORCH, LIT_WALL_TORCH, new FabricItemSettings().group(ItemGroup.MISC), ETorchState.LIT, config.defaultTorchFuel));
		Registry.register(Registry.ITEM, new Identifier("hardcore_torches", "unlit_torch"), new TorchItem(UNLIT_TORCH, UNLIT_WALL_TORCH, new FabricItemSettings().group(ItemGroup.MISC), ETorchState.UNLIT, config.defaultTorchFuel));
		Registry.register(Registry.ITEM, new Identifier("hardcore_torches", "smoldering_torch"), new TorchItem(SMOLDERING_TORCH, SMOLDERING_WALL_TORCH, new FabricItemSettings().group(ItemGroup.MISC), ETorchState.SMOLDERING, config.defaultTorchFuel));
		Registry.register(Registry.ITEM, new Identifier("hardcore_torches", "burnt_torch"), new TorchItem(BURNT_TORCH, BURNT_WALL_TORCH, new FabricItemSettings().group(ItemGroup.MISC), ETorchState.BURNT, config.defaultTorchFuel));

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
				1
			);

	}
}
