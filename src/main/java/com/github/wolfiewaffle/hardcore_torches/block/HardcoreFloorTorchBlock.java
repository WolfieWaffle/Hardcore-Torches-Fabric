//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.block;

import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import java.util.function.IntSupplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class HardcoreFloorTorchBlock extends AbstractHardcoreTorchBlock implements BlockEntityProvider {
  public HardcoreFloorTorchBlock(AbstractBlock.Settings settings, ParticleEffect particle, ETorchState type, IntSupplier maxFuel) {
    super(settings, particle, type, maxFuel);
  }

  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return Blocks.TORCH.getOutlineShape(state, world, pos, context);
  }

  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    return Blocks.TORCH.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
  }

  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    return Blocks.TORCH.canPlaceAt(state, world, pos);
  }

  public boolean isWall() {
    return false;
  }
}
