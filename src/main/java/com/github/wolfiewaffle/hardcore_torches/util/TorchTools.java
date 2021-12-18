package com.github.wolfiewaffle.hardcore_torches.util;

import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class TorchTools {

    public static void displayParticle(ParticleEffect particle, BlockState state, World world, BlockPos pos, float spread) {
        double d = (double)pos.getX() + 0.5;
        double e = (double)pos.getY() + 0.7;
        double f = (double)pos.getZ() + 0.5;

        if (particle != null) {
            if (state.contains(Properties.HORIZONTAL_FACING)) {
                Direction dir = state.get(Properties.HORIZONTAL_FACING);
                Direction dir2 = dir.getOpposite();

                if (world instanceof  ServerWorld) {
                    ((ServerWorld) world).spawnParticles(particle, d + 0.27 * (double) dir2.getOffsetX(), e + 0.22, f + 0.27 * (double) dir2.getOffsetZ(), 1, 0, 0, 0, 0);
                } else if (world.isClient) {
                    world.addParticle(particle, d + 0.27 * (double) dir2.getOffsetX(), e + 0.22, f + 0.27 * (double) dir2.getOffsetZ(), 0.0, 0.0, 0.0);
                }
            } else {
                if (world instanceof  ServerWorld) {
                    ((ServerWorld) world).spawnParticles(particle, d, e, f, 1, 0, 0, 0, 0);
                } else if (world.isClient) {
                    world.addParticle(particle, d, e, f, 0.0, 0.0, 0.0);
                }
            }
        }
     }

    public static void displayParticle(ParticleEffect particle, BlockState state, World world, BlockPos pos) {
        displayParticle(particle, state, world, pos, 0f);
    }
}
