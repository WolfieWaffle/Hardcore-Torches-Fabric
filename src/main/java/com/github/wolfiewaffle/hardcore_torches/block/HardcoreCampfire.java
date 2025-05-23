package com.github.wolfiewaffle.hardcore_torches.block;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.blockentity.HardcoreCampfireBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.blockentity.IFuelBlock;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

public class HardcoreCampfire extends CampfireBlock implements IFuelBlock {

    public HardcoreCampfire(boolean emitsParticles, int fireDamage, Settings settings) {
        super(emitsParticles, fireDamage, settings);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        WorldAccess worldAccess = context.getWorld();
        BlockPos blockpos = context.getBlockPos();
        boolean flag = worldAccess.getFluidState(blockpos).getFluid() == Fluids.WATER;
        return this.getDefaultState().with(WATERLOGGED, flag).with(SIGNAL_FIRE, this.isSignalFireBaseBlock(worldAccess.getBlockState(blockpos.down()))).with(LIT, false).with(FACING, context.getHorizontalPlayerFacing());
    }

    private boolean isSignalFireBaseBlock(BlockState state) {
        return state.isOf(Blocks.HAY_BLOCK);
    }

    @Override
    public void outOfFuel(World world, BlockPos pos, BlockState state, boolean playSound) {
        BlockState newState = state.with(CampfireBlock.LIT, false);
        world.setBlockState(pos, newState);
    }

    public void light(World world, BlockPos pos, BlockState state) {
        BlockState newState = state.with(CampfireBlock.LIT, true);
        world.setBlockState(pos, newState);
        world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1f, 1f);
    }

    public boolean canLight(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity != null && blockEntity instanceof HardcoreCampfireBlockEntity campfire) {
            if (campfire.getFuel() <= 0) return false;
        }
        //return canBeLit(world.getBlockState(pos)); not working for some reason
        BlockState state = world.getBlockState(pos);
        return !state.get(WATERLOGGED) && !state.get(LIT);
    }

    public int getFuel(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity != null && blockEntity instanceof HardcoreCampfireBlockEntity campfire) {
            return campfire.getFuel();
        }
        return 0;
    }

    public static ToIntFunction<BlockState> litBlockEmission(int max) {
        return (value) -> {
            return value.get(CampfireBlock.LIT) ? max : 0;
        };
    }

    public ActionResult needsFuel(PlayerEntity player) {
        player.sendMessage(Text.literal("Drop combustible items on top to add fuel!"), true);
        return ActionResult.CONSUME;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new HardcoreCampfireBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient) {
            return state.get(LIT) ? checkType(type, Mod.CAMPFIRE_BLOCK_ENTITY, HardcoreCampfireBlockEntity::clientTick) : null;
        } else {
            return state.get(LIT) ? checkType(type, Mod.CAMPFIRE_BLOCK_ENTITY, HardcoreCampfireBlockEntity::cookTick) : checkType(type, Mod.CAMPFIRE_BLOCK_ENTITY, HardcoreCampfireBlockEntity::cooldownTick);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);

        // Override flint and steel. This may have to change if more blocks become able to light campfires.
        if (stack.getItem() instanceof FlintAndSteelItem) {
            if (state.get(CampfireBlock.LIT)) return super.onUse(state, world, pos, player, hand, hit);

            if (!world.isClient) {
                if (getFuel(world, pos) <= 0) {
                    return needsFuel(player);
                } else if (world.getBlockEntity(pos) instanceof HardcoreCampfireBlockEntity campfire) {
                    if (attemptUseItem(stack, player, hand, ETorchState.LIT)) {
                        light(world, pos, state);
                        player.swingHand(hand);
                        return ActionResult.SUCCESS;
                    }
                }
            }

            return ActionResult.CONSUME;
        }

        if (itemValid(stack, getFreeLightItems(), getDamageLightItems(), getConsumeLightItems())) {
            if (state.get(CampfireBlock.LIT)) return super.onUse(state, world, pos, player, hand, hit);

            if (!world.isClient) {
                if (getFuel(world, pos) <= 0) {
                    return needsFuel(player);
                } else if (world.getBlockEntity(pos) instanceof HardcoreCampfireBlockEntity campfire) {
                    if (attemptUseItem(stack, player, hand, ETorchState.LIT)) {
                        light(world, pos, state);
                        player.swingHand(hand);
                        return ActionResult.SUCCESS;
                    }
                }
            }

            return ActionResult.CONSUME;
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public boolean attemptUse(ItemStack stack, PlayerEntity player, Hand hand, TagKey free, TagKey damage, TagKey consume) {
        return IFuelBlock.super.attemptUse(stack, player, hand, free, damage, consume);
    }
}
