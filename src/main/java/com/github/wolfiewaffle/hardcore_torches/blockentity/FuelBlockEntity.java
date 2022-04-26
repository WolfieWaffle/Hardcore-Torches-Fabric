package com.github.wolfiewaffle.hardcore_torches.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class FuelBlockEntity extends BlockEntity {
    protected int fuel;
    protected static Random random = new Random();

    public FuelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // Serialize the BlockEntity
    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        // Save the current value of the number to the tag
        tag.putInt("Fuel", fuel);
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        if (tag.contains("number")) {
            fuel = tag.getInt("number");
        } else {
            fuel = tag.getInt("Fuel");
        }
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int newValue) {
        fuel = newValue;
    }

    public void changeFuel(int increment) {
        World world = this.getWorld();
        BlockPos pos = this.getPos();

        fuel += increment;

        if (fuel <= 0) {
            fuel = 0;

            if (world.getBlockState(pos).getBlock() instanceof IFuelBlock) {
                IFuelBlock block = (IFuelBlock) world.getBlockState(pos).getBlock();
                block.outOfFuel(world, pos, world.getBlockState(pos), false);
            }
        }
    }
}
