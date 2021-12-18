package com.github.wolfiewaffle.hardcore_torches.block;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.blockentity.TorchBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.config.HardcoreTorchesConfig;
import com.github.wolfiewaffle.hardcore_torches.item.TorchItem;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import com.github.wolfiewaffle.hardcore_torches.util.TorchGroup;
import com.github.wolfiewaffle.hardcore_torches.util.TorchTools;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public abstract class AbstractHardcoreTorchBlock extends BlockWithEntity implements BlockEntityProvider {

    public ParticleEffect particle;
    public ETorchState burnState;
    public TorchGroup group;

    public static final float pX = 0.5f;
    public static final float pY = 0.7f;
    public static final float pZ = 0.5f;

    public AbstractHardcoreTorchBlock(AbstractBlock.Settings settings, ParticleEffect particle, ETorchState type) {
        super(settings);
        this.particle = particle;
        this.burnState = type;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (burnState == ETorchState.LIT || burnState == ETorchState.SMOLDERING) {
            TorchTools.displayParticle(ParticleTypes.SMOKE, state, world, pos);
        }

        if (burnState == ETorchState.LIT) {
            TorchTools.displayParticle(this.particle, state, world, pos);
        }
    }

    public void smother(World world, BlockPos pos, BlockState state) {
        if (!world.isClient) {
            world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1f);
            TorchTools.displayParticle(ParticleTypes.LARGE_SMOKE, state, world, pos);
            TorchTools.displayParticle(ParticleTypes.LARGE_SMOKE, state, world, pos);
            TorchTools.displayParticle(ParticleTypes.SMOKE, state, world, pos);
            TorchTools.displayParticle(ParticleTypes.SMOKE, state, world, pos);
            changeTorch(world, pos, state, ETorchState.SMOLDERING);
        }
    }

    public void extinguish(World world, BlockPos pos, BlockState state) {
        if (!world.isClient) {
            world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1f);
            TorchTools.displayParticle(ParticleTypes.LARGE_SMOKE, state, world, pos);
            TorchTools.displayParticle(ParticleTypes.LARGE_SMOKE, state, world, pos);
            TorchTools.displayParticle(ParticleTypes.SMOKE, state, world, pos);
            TorchTools.displayParticle(ParticleTypes.SMOKE, state, world, pos);
            changeTorch(world, pos, state, ETorchState.UNLIT);
        }
    }

    public void burnOut(World world, BlockPos pos, BlockState state) {
        if (!world.isClient) {
            world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1f);
            TorchTools.displayParticle(ParticleTypes.LARGE_SMOKE, state, world, pos);
            TorchTools.displayParticle(ParticleTypes.LARGE_SMOKE, state, world, pos);
            TorchTools.displayParticle(ParticleTypes.SMOKE, state, world, pos);
            TorchTools.displayParticle(ParticleTypes.SMOKE, state, world, pos);
            changeTorch(world, pos, state, ETorchState.BURNT);
        }
    }

    public void light(World world, BlockPos pos, BlockState state) {
        if (!world.isClient) {
            world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 0.5f, 1.2f);
            TorchTools.displayParticle(ParticleTypes.LAVA, state, world, pos);
            TorchTools.displayParticle(ParticleTypes.FLAME, state, world, pos);
            changeTorch(world, pos, state, ETorchState.LIT);
        }
    }

    public abstract boolean isWall();

    public ETorchState getBurnState() {
        return burnState;
    }

    public void changeTorch(World world, BlockPos pos, BlockState curState, ETorchState newType) {
        BlockState newState;

        if (isWall()) {
            newState = group.getWallTorch(newType).getDefaultState().with(HorizontalFacingBlock.FACING, curState.get(HardcoreWallTorchBlock.FACING));
        } else {
            newState = group.getStandingTorch(newType).getDefaultState();
        }

        int newFuel = 0;
        if (world.getBlockEntity(pos) != null) newFuel = ((TorchBlockEntity) world.getBlockEntity(pos)).getFuel();
        world.setBlockState(pos, newState);
        if (world.getBlockEntity(pos) != null) ((TorchBlockEntity) world.getBlockEntity(pos)).setFuel(newFuel);
    }

    // region BlockEntity code
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TorchBlockEntity(pos, state);
    }

    // Is invisible without this
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }

    // Needed for ticking, idk what it means
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, Mod.TORCH_BLOCK_ENTITY, (world1, pos, state1, be) -> TorchBlockEntity.tick(world1, pos, state1, be));
    }
    //endregion

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        boolean success = false;

        // Fuel message
//        BlockEntity be = world.getBlockEntity(pos);
//        if (be.getType() == Mod.TORCH_BLOCK_ENTITY && !world.isClient) {
//            player.sendMessage(new LiteralText("Fuel: " + ((TorchBlockEntity) be).getFuel()), true);
//        }

        if (burnState == ETorchState.LIT) {

            // Infinite extinguish items
            if (HardcoreTorchesConfig.getItems(Mod.config.freeExtinguishItems).contains(stack.getItem())) {
                extinguish(world, pos, state);
                success = true;
            }
        }

        if (burnState == ETorchState.SMOLDERING || burnState == ETorchState.UNLIT) {

            // Infinite light items
            if (HardcoreTorchesConfig.getItems(Mod.config.freeLightItems).contains(stack.getItem())) {
                light(world, pos, state);
                success = true;
            }

            // Durability light items
            if (success == false && HardcoreTorchesConfig.getItems(Mod.config.damageLightItems).contains(stack.getItem())) {
                if (stack.isDamageable()) {
                    stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
                }
                light(world, pos, state);
                success = true;
            }

            // Consume light items
            if (success == false && HardcoreTorchesConfig.getItems(Mod.config.consumeLightItems).contains(stack.getItem())) {
                if (!player.isCreative()) {
                    stack.decrement(1);
                }
                light(world, pos, state);
                success = true;
            }
        }

        if (success) {
            player.swingHand(hand);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        BlockEntity be = world.getBlockEntity(pos);

        if (be != null && be instanceof TorchBlockEntity && itemStack.getItem() instanceof TorchItem) {
            int fuel = TorchItem.getFuel(itemStack);

            if (fuel == 0) {
                ((TorchBlockEntity) be).setFuel(Mod.config.defaultTorchFuel);
            } else {
                ((TorchBlockEntity) be).setFuel(fuel);
            }
        }
    }
}
