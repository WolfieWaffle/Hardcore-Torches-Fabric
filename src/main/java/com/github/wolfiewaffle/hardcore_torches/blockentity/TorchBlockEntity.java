package com.github.wolfiewaffle.hardcore_torches.blockentity;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.block.AbstractHardcoreTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TorchBlockEntity extends FuelBlockEntity {

    public TorchBlockEntity(BlockPos pos, BlockState state) {
        super(Mod.TORCH_BLOCK_ENTITY, pos, state);
        fuel = Mod.config.defaultTorchFuel;
    }

    public static void tick(World world, BlockPos pos, BlockState state, HardcoreCampfireBlockEntity be) {
        if (!world.isClient) {
            if (!(state.getBlock() instanceof AbstractHardcoreTorchBlock)) return;
            if (((AbstractHardcoreTorchBlock) state.getBlock()).getBurnState() == ETorchState.LIT) {
                tickLit(world, pos, state, be);
            } else if (((AbstractHardcoreTorchBlock) state.getBlock()).getBurnState() == ETorchState.SMOLDERING) {
                tickSmoldering(world, pos, state, be);
            }
        }
    }

    private static void tickLit(World world, BlockPos pos, BlockState state, HardcoreCampfireBlockEntity be) {

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
                ((AbstractHardcoreTorchBlock) world.getBlockState(pos).getBlock()).outOfFuel(world, pos, state, false);
            }
        }

        be.markDirty();
    }

        private static void tickSmoldering(World world, BlockPos pos, BlockState state, HardcoreCampfireBlockEntity be) {

        // Burn out
        if (random.nextInt(3) == 0) {
            if (be.fuel > 0) {
                be.fuel--;

                if (be.fuel <= 0) {
                    ((AbstractHardcoreTorchBlock) world.getBlockState(pos).getBlock()).burnOut(world, pos, state, false);
                }
            }
        }

        be.markDirty();
    }
}
