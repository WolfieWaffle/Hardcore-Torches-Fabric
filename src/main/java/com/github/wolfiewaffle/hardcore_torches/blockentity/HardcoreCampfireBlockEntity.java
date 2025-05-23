package com.github.wolfiewaffle.hardcore_torches.blockentity;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.block.HardcoreCampfire;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;

public class HardcoreCampfireBlockEntity extends CampfireBlockEntity {
    protected int fuel;

    public HardcoreCampfireBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
        fuel = 0;
    }

    private enum FuelResult {
        HAD_SPACE,
        SOME_WASTE,
        FULL
    }

    @Override
    public BlockEntityType<?> getType() {
        return Mod.CAMPFIRE_BLOCK_ENTITY;
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, HardcoreCampfireBlockEntity entity) {
        CampfireBlockEntity.clientTick(world, pos, state, entity);
    }

    public static void cookTick(World world, BlockPos pos, BlockState state, HardcoreCampfireBlockEntity entity) {

        // Decrease fuel
        if (entity.fuel <= 0) {
            if (state.getBlock() instanceof HardcoreCampfire campfire) {
                campfire.outOfFuel(world, pos, state, true);
            }
        } else {
            entity.fuel--;
        }

        takeFuelItems(world, pos, entity);

        if (world.isChunkLoaded(pos)) {
            world.markDirty(pos);
        }

        CampfireBlockEntity.litServerTick(world, pos, state, entity);
    }

    public static void cooldownTick(World world, BlockPos pos, BlockState state, HardcoreCampfireBlockEntity entity) {
        takeFuelItems(world, pos, entity);

        if (world.isChunkLoaded(pos)) {
            world.markDirty(pos);
        }

        CampfireBlockEntity.unlitServerTick(world, pos, state, entity);
    }

    public static boolean isLit(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        if (state.contains(CampfireBlock.LIT)) return state.get(CampfireBlock.LIT);

        return false;
    }

    private static void takeFuelItems(World world, BlockPos pos, HardcoreCampfireBlockEntity entity) {
        for(ItemEntity itementity : getItemsAtAndAbove(world, pos)) {
            Integer burnTime = FuelRegistry.INSTANCE.get(itementity.getStack().getItem());
            if (burnTime == null) continue;
            if (burnTime > 0) {
                FuelResult result = entity.setFuel((int) (entity.getFuel() + (burnTime * Mod.config.campfireFuelFactor)));

                if (result != FuelResult.FULL) {
                    itementity.kill();
                    world.playSound(null, pos, SoundEvents.ITEM_BONE_MEAL_USE, SoundCategory.BLOCKS, 1f, 1f);
                    if (result == FuelResult.SOME_WASTE && isLit(world, pos)) world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1.5f);
                }
            }
        }
    }

    public int getFuel() {
        return fuel;
    }

    public FuelResult setFuel(int newValue) {
        if (fuel >= Mod.config.maxCampfireFuel) return FuelResult.FULL;

        fuel = newValue;
        if (fuel > Mod.config.maxCampfireFuel) {
            fuel = Mod.config.maxCampfireFuel;
            return FuelResult.SOME_WASTE;
        }

        return FuelResult.HAD_SPACE;
    }

    public static List<ItemEntity> getItemsAtAndAbove(World world, BlockPos pos) {
        VoxelShape INSIDE_SHAPE = Block.createCuboidShape(2.0D, 11.0D, 2.0D, 14.0D, 16.0D, 14.0D);
        VoxelShape ABOVE_SHAPE = Block.createCuboidShape(0.0D, 16.0D, 0.0D, 16.0D, 32.0D, 16.0D);
        VoxelShape INPUT_AREA_SHAPE = VoxelShapes.union(INSIDE_SHAPE, ABOVE_SHAPE);

        return INPUT_AREA_SHAPE.getBoundingBoxes().stream().flatMap((box) -> world.getEntitiesByClass(ItemEntity.class, box.offset(pos.getX() - 0.5D, pos.getY() - 0.5D, pos.getZ() - 0.5D), EntityPredicates.VALID_ENTITY).stream()).collect(Collectors.toList());
    }

    // region necessary methods
    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt != null) {
            super.readNbt(nbt);

            fuel = nbt.getInt("Fuel");
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putInt("Fuel", fuel);
    }
    // endregion
}
