//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.block;

import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import java.util.function.IntSupplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class HardcoreWallTorchBlock extends AbstractHardcoreTorchBlock implements BlockEntityProvider {
  public static final DirectionProperty FACING;

  public HardcoreWallTorchBlock(AbstractBlock.Settings settings, ParticleEffect particle, ETorchState type, IntSupplier maxFuel) {
    super(settings, particle, type, maxFuel);
    this.setDefaultState((BlockState)((BlockState)this.getStateManager().getDefaultState()).with(FACING, Direction.NORTH));
  }

  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return Blocks.WALL_TORCH.getOutlineShape(state, world, pos, context);
  }

  public static VoxelShape getBoundingShape(BlockState state) {
    return WallTorchBlock.getBoundingShape(state);
  }

  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    return Blocks.WALL_TORCH.canPlaceAt(state, world, pos);
  }

  public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
    BlockState torchState = Blocks.WALL_TORCH.getPlacementState(ctx);
    if (torchState != null) {
      BlockState state = this.getDefaultState();
      Direction d = (Direction)torchState.get(FACING);
      return (BlockState)state.with(FACING, d);
    } else {
      return null;
    }
  }

  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    return Blocks.WALL_TORCH.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
  }

  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return Blocks.WALL_TORCH.rotate(state, rotation);
  }

  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return Blocks.WALL_TORCH.mirror(state, mirror);
  }

  protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
    stateManager.add(new Property[]{FACING});
  }

  public void smother(World world, BlockPos pos, BlockState state) {
    super.smother(world, pos, state);
    HardcoreWallTorchBlock newTorch = this.group.getWallTorch(ETorchState.SMOLDERING);
    world.setBlockState(pos, (BlockState)newTorch.getDefaultState().with(HorizontalFacingBlock.FACING, (Direction)state.get(FACING)));
  }

  public void burnOut(World world, BlockPos pos, BlockState state, boolean playSound) {
    super.burnOut(world, pos, state, playSound);
    HardcoreWallTorchBlock newTorch = this.group.getWallTorch(ETorchState.BURNT);
    world.setBlockState(pos, (BlockState)newTorch.getDefaultState().with(HorizontalFacingBlock.FACING, (Direction)state.get(FACING)));
  }

  public void light(World world, BlockPos pos, BlockState state) {
    super.light(world, pos, state);
    HardcoreWallTorchBlock newTorch = this.group.getWallTorch(ETorchState.LIT);
    world.setBlockState(pos, (BlockState)newTorch.getDefaultState().with(HorizontalFacingBlock.FACING, (Direction)state.get(FACING)));
  }

  public boolean isWall() {
    return true;
  }

  static {
    FACING = HorizontalFacingBlock.FACING;
  }
}
