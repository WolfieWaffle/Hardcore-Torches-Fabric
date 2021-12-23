package com.github.wolfiewaffle.hardcore_torches.blockentity;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.block.AbstractHardcoreTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class FuelBlockEntity extends BlockEntity {

    // Store the current value of the number
    private int fuel;
    private static Random random = new Random();

    public FuelBlockEntity(BlockPos pos, BlockState state) {
        super(Mod.TORCH_BLOCK_ENTITY, pos, state);
        fuel = Mod.config.defaultTorchFuel;
    }

    // Serialize the BlockEntity
    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        // Save the current value of the number to the tag
        tag.putInt("number", fuel);
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        fuel = tag.getInt("number");
    }

    public static void tick(World world, BlockPos pos, BlockState state, FuelBlockEntity be) {
        if (!world.isClient) {
            if (((AbstractHardcoreTorchBlock) state.getBlock()).getBurnState() == ETorchState.LIT) {
                tickLit(world, pos, state, be);
            } else if (((AbstractHardcoreTorchBlock) state.getBlock()).getBurnState() == ETorchState.SMOLDERING) {
                tickSmoldering(world, pos, state, be);
            }
        }
    }

    private static void tickLit(World world, BlockPos pos, BlockState state, FuelBlockEntity be) {

        // Extinguish
        if (Mod.config.torchesRain && world.hasRain(pos)) {
            if (random.nextInt(200) == 0) {
                if (Mod.config.torchesSmolder) {
                    ((AbstractHardcoreTorchBlock) world.getBlockState(pos).getBlock()).smother(world, pos, state);
                } else {
                    ((AbstractHardcoreTorchBlock) world.getBlockState(pos).getBlock()).extinguish(world, pos, state);
                }
            }
        }

        // Burn out
        if (be.fuel > 0) {
            be.fuel--;

            if (be.fuel <= 0) {
                ((AbstractHardcoreTorchBlock) world.getBlockState(pos).getBlock()).burnOut(world, pos, state);
            }
        }

        be.markDirty();
    }

        private static void tickSmoldering(World world, BlockPos pos, BlockState state, FuelBlockEntity be) {

        // Burn out
        if (random.nextInt(3) == 0) {
            if (be.fuel > 0) {
                be.fuel--;

                if (be.fuel <= 0) {
                    ((AbstractHardcoreTorchBlock) world.getBlockState(pos).getBlock()).burnOut(world, pos, state);
                }
            }
        }

        be.markDirty();
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int newValue) {
        fuel = newValue;
    }
}
