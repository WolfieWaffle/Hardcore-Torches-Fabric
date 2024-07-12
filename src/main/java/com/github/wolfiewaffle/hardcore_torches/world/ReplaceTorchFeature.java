package com.github.wolfiewaffle.hardcore_torches.world;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.blockentity.LanternBlockEntity;
import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.HashMap;
import java.util.Map;

public class ReplaceTorchFeature extends Feature<ReplaceTorchFeatureConfig> {
    private static Map<BlockState, BlockState> REPLACEMENTS = new HashMap<>();
    private static BlockState TORCH_STATE = Blocks.TORCH.getDefaultState();
    private static BlockState TORCH_STATE_EAST = Blocks.WALL_TORCH.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.EAST);
    private static BlockState TORCH_STATE_NORTH = Blocks.WALL_TORCH.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.NORTH);
    private static BlockState TORCH_STATE_SOUTH = Blocks.WALL_TORCH.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.SOUTH);
    private static BlockState TORCH_STATE_WEST = Blocks.WALL_TORCH.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.WEST);
    private static BlockState LANTERN_STATE = Blocks.LANTERN.getDefaultState();
    private static BlockState LANTERN_STATE_HANGING = Blocks.LANTERN.getDefaultState().with(Properties.HANGING, true);

    public ReplaceTorchFeature(Codec<ReplaceTorchFeatureConfig> configCodec) {
        super(configCodec);
    }

    static {
        REPLACEMENTS.put(TORCH_STATE, Mod.LIT_TORCH.getDefaultState());
        REPLACEMENTS.put(TORCH_STATE_EAST, Mod.LIT_WALL_TORCH.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.EAST));
        REPLACEMENTS.put(TORCH_STATE_NORTH, Mod.LIT_WALL_TORCH.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.NORTH));
        REPLACEMENTS.put(TORCH_STATE_SOUTH, Mod.LIT_WALL_TORCH.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.SOUTH));
        REPLACEMENTS.put(TORCH_STATE_WEST, Mod.LIT_WALL_TORCH.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.WEST));
        REPLACEMENTS.put(LANTERN_STATE, Mod.LIT_LANTERN.getDefaultState());
        REPLACEMENTS.put(LANTERN_STATE_HANGING, Mod.LIT_LANTERN.getDefaultState().with(Properties.HANGING, true));
    }

    // this method is what is called when the game tries to generate the feature. it is where the actual blocks get placed into the world.
    @Override
    public boolean generate(FeatureContext<ReplaceTorchFeatureConfig> context) {
        BlockPos origin = context.getOrigin();
        StructureWorldAccess level = context.getWorld();
        Chunk chunk = level.getChunk(origin);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < chunk.getHeight(); y++) {
                    BlockPos pos = new BlockPos(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                    BlockState oldState = chunk.getBlockState(pos);
                    BlockState newState = REPLACEMENTS.get(oldState);

                    if (newState != null) {
                        chunk.setBlockState(pos, newState, false);
                        Block newBlock = newState.getBlock();

                        if (newBlock instanceof BlockEntityProvider) {
                            BlockEntity newEntity = ((BlockEntityProvider) chunk.getBlockState(pos).getBlock()).createBlockEntity(pos, newState);
                            chunk.setBlockEntity(newEntity);

                            if (newEntity instanceof LanternBlockEntity) {
                                ((LanternBlockEntity) newEntity).setFuel(Mod.config.defaultLanternFuel);
                            }

                            newEntity.markDirty();
                        }
                    }
                }
            }
        }

        return true;
    }
}
