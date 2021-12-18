package com.github.wolfiewaffle.hardcore_torches.block;

import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import net.minecraft.block.*;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class HardcoreFloorTorchBlock extends AbstractHardcoreTorchBlock implements BlockEntityProvider {

    public HardcoreFloorTorchBlock(Settings settings, ParticleEffect particle, ETorchState type) {
        super(settings, particle, type);
    }

    // region Overridden methods for TorchBlock since I can't extend 2 classes
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Blocks.TORCH.getOutlineShape(state, world, pos, context);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return Blocks.TORCH.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return Blocks.TORCH.canPlaceAt(state, world, pos);
    }
    // endregion

    @Override
    public boolean isWall() { return false; }
}