//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.world;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.blockentity.LanternBlockEntity;
import com.mojang.serialization.Codec;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class ReplaceTorchFeature extends Feature<ReplaceTorchFeatureConfig> {
  private static Map<BlockState, BlockState> REPLACEMENTS = new HashMap();
  private static BlockState TORCH_STATE;
  private static BlockState TORCH_STATE_EAST;
  private static BlockState TORCH_STATE_NORTH;
  private static BlockState TORCH_STATE_SOUTH;
  private static BlockState TORCH_STATE_WEST;
  private static BlockState LANTERN_STATE;
  private static BlockState LANTERN_STATE_HANGING;

  public ReplaceTorchFeature(Codec<ReplaceTorchFeatureConfig> configCodec) {
    super(configCodec);
  }

  public boolean generate(FeatureContext<ReplaceTorchFeatureConfig> context) {
    BlockPos origin = context.getOrigin();
    StructureWorldAccess level = context.getWorld();
    Chunk chunk = level.getChunk(origin);

    for(int x = 0; x < 16; ++x) {
      for(int z = 0; z < 16; ++z) {
        for(int y = 0; y < chunk.getHeight(); ++y) {
          BlockPos pos = new BlockPos(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
          BlockState oldState = chunk.getBlockState(pos);
          BlockState newState = (BlockState)REPLACEMENTS.get(oldState);
          if (newState != null) {
            chunk.setBlockState(pos, newState, false);
            Block newBlock = newState.getBlock();
            if (newBlock instanceof BlockWithEntity) {
              BlockEntity newEntity = ((BlockWithEntity)chunk.getBlockState(pos).getBlock()).createBlockEntity(pos, newState);
              chunk.setBlockEntity(newEntity);
              if (newEntity instanceof LanternBlockEntity) {
                ((LanternBlockEntity)newEntity).setFuel(Mod.config.defaultLanternFuel);
              }

              newEntity.markDirty();
            }
          }
        }
      }
    }

    return true;
  }

  static {
    TORCH_STATE = Blocks.TORCH.getDefaultState();
    TORCH_STATE_EAST = (BlockState)Blocks.WALL_TORCH.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.EAST);
    TORCH_STATE_NORTH = (BlockState)Blocks.WALL_TORCH.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.NORTH);
    TORCH_STATE_SOUTH = (BlockState)Blocks.WALL_TORCH.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.SOUTH);
    TORCH_STATE_WEST = (BlockState)Blocks.WALL_TORCH.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.WEST);
    LANTERN_STATE = Blocks.LANTERN.getDefaultState();
    LANTERN_STATE_HANGING = (BlockState)Blocks.LANTERN.getDefaultState().with(Properties.HANGING, true);
    REPLACEMENTS.put(TORCH_STATE, Mod.LIT_TORCH.getDefaultState());
    REPLACEMENTS.put(TORCH_STATE_EAST, (BlockState)Mod.LIT_WALL_TORCH.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.EAST));
    REPLACEMENTS.put(TORCH_STATE_NORTH, (BlockState)Mod.LIT_WALL_TORCH.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.NORTH));
    REPLACEMENTS.put(TORCH_STATE_SOUTH, (BlockState)Mod.LIT_WALL_TORCH.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.SOUTH));
    REPLACEMENTS.put(TORCH_STATE_WEST, (BlockState)Mod.LIT_WALL_TORCH.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.WEST));
    REPLACEMENTS.put(LANTERN_STATE, Mod.LIT_LANTERN.getDefaultState());
    REPLACEMENTS.put(LANTERN_STATE_HANGING, (BlockState)Mod.LIT_LANTERN.getDefaultState().with(Properties.HANGING, true));
  }
}
