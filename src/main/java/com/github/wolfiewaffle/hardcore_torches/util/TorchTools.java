//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.util;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.item.TorchItem;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class TorchTools {
  public TorchTools() {
  }

  public static boolean canLight(Item item, BlockState blockState) {
    if (item instanceof TorchItem) {
      ETorchState state = ((TorchItem)item).getTorchState();
      if ((state == ETorchState.UNLIT || state == ETorchState.SMOLDERING) && blockState.isIn(Mod.FREE_TORCH_LIGHT_BLOCKS)) {
        return true;
      }
    }

    return false;
  }

  public static void displayParticle(ParticleEffect particle, BlockState state, World world, BlockPos pos, float spread) {
    double d = (double)pos.getX() + (double)0.5F;
    double e = (double)pos.getY() + 0.7;
    double f = (double)pos.getZ() + (double)0.5F;
    if (particle != null) {
      if (state.contains(Properties.HORIZONTAL_FACING)) {
        Direction dir = (Direction)state.get(Properties.HORIZONTAL_FACING);
        Direction dir2 = dir.getOpposite();
        if (world instanceof ServerWorld) {
          ((ServerWorld)world).spawnParticles(particle, d + 0.27 * (double)dir2.getOffsetX(), e + 0.22, f + 0.27 * (double)dir2.getOffsetZ(), 1, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F);
        } else if (world.isClient) {
          world.addParticle(particle, d + 0.27 * (double)dir2.getOffsetX(), e + 0.22, f + 0.27 * (double)dir2.getOffsetZ(), (double)0.0F, (double)0.0F, (double)0.0F);
        }
      } else if (world instanceof ServerWorld) {
        ((ServerWorld)world).spawnParticles(particle, d, e, f, 1, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F);
      } else if (world.isClient) {
        world.addParticle(particle, d, e, f, (double)0.0F, (double)0.0F, (double)0.0F);
      }
    }

  }

  public static void displayParticle(ParticleEffect particle, BlockState state, World world, BlockPos pos) {
    displayParticle(particle, state, world, pos, 0.0F);
  }
}
