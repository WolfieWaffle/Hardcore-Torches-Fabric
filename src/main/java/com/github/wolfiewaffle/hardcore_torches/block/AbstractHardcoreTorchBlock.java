package com.github.wolfiewaffle.hardcore_torches.block;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.blockentity.FuelBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.blockentity.IFuelBlock;
import com.github.wolfiewaffle.hardcore_torches.blockentity.HardcoreCampfireBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.blockentity.TorchBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.item.OilCanItem;
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
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntSupplier;

public abstract class AbstractHardcoreTorchBlock extends Block implements BlockEntityProvider, IFuelBlock {

    public ParticleEffect particle;
    public ETorchState burnState;
    public TorchGroup group;
    public IntSupplier maxFuel;

    public AbstractHardcoreTorchBlock(AbstractBlock.Settings settings, ParticleEffect particle, ETorchState type, IntSupplier maxFuel) {
        super(settings);
        this.particle = particle;
        this.burnState = type;
        this.maxFuel = maxFuel;
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

    public void burnOut(World world, BlockPos pos, BlockState state, boolean playSound) {
        if (!world.isClient) {
            if (playSound) world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1f);
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
        if (world.getBlockEntity(pos) != null) newFuel = ((FuelBlockEntity) world.getBlockEntity(pos)).getFuel();
        world.setBlockState(pos, newState);
        if (world.getBlockEntity(pos) != null) ((FuelBlockEntity) world.getBlockEntity(pos)).setFuel(newFuel);
    }

    public static boolean isLightItem(ItemStack stack) {
        if (stack.isIn(Mod.FREE_TORCH_LIGHT_ITEMS)) return true;
        if (stack.isIn(Mod.DAMAGE_TORCH_LIGHT_ITEMS)) return true;
        if (stack.isIn(Mod.CONSUME_TORCH_LIGHT_ITEMS)) return true;
        return false;
    }

    // region BlockEntity code
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new HardcoreCampfireBlockEntity(pos, state);
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
        return type == Mod.TORCH_BLOCK_ENTITY ? (world1, pos, state1, be) -> TorchBlockEntity.tick(world1, pos, state1, (HardcoreCampfireBlockEntity) be) : null;
    }
    //endregion

    // region Overridden methods
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (burnState == ETorchState.LIT || burnState == ETorchState.SMOLDERING) {
            TorchTools.displayParticle(ParticleTypes.SMOKE, state, world, pos);
        }

        if (burnState == ETorchState.LIT) {
            TorchTools.displayParticle(this.particle, state, world, pos);
        }
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        boolean success = false;

        if (burnState == ETorchState.LIT) {
            if (attemptUse(stack, player, hand, Mod.FREE_TORCH_EXTINGUISH_ITEMS, Mod.DAMAGE_TORCH_EXTINGUISH_ITEMS, Mod.CONSUME_TORCH_EXTINGUISH_ITEMS)) {
                extinguish(world, pos, state);
                player.swingHand(hand);
                return ActionResult.SUCCESS;
            }

            if (attemptUse(stack, player, hand, Mod.FREE_TORCH_SMOTHER_ITEMS, Mod.DAMAGE_TORCH_SMOTHER_ITEMS, Mod.CONSUME_TORCH_SMOTHER_ITEMS)) {
                smother(world, pos, state);
                player.swingHand(hand);
                return ActionResult.SUCCESS;
            }
        }

        if (burnState == ETorchState.SMOLDERING || burnState == ETorchState.UNLIT) {
            if (attemptUse(stack, player, hand, Mod.FREE_TORCH_LIGHT_ITEMS, Mod.DAMAGE_TORCH_LIGHT_ITEMS, Mod.CONSUME_TORCH_LIGHT_ITEMS)) {
                light(world, pos, state);
                player.swingHand(hand);
                return ActionResult.SUCCESS;
            }
        }

        // Fuel message
        BlockEntity be = world.getBlockEntity(pos);
        if (be.getType() == Mod.TORCH_BLOCK_ENTITY && !world.isClient && Mod.config.fuelMessage && stack.isEmpty()) {
            player.sendMessage(Text.of("Fuel: " + ((HardcoreCampfireBlockEntity) be).getFuel()), true);
        }

        // Oil Can
        if (Mod.config.torchesUseCan && burnState != ETorchState.BURNT && !world.isClient) {
            if (OilCanItem.fuelBlock((FuelBlockEntity) be, world, stack)) {
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1f, 1f);
            }
        }

        // Hand extinguish
        if (Mod.config.handUnlightTorch && (burnState == ETorchState.LIT || burnState == ETorchState.SMOLDERING)) {
            if (!TorchTools.canLight(stack.getItem(), state)) {
                extinguish(world, pos, state);
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        BlockEntity be = world.getBlockEntity(pos);

        if (be != null && be instanceof FuelBlockEntity && itemStack.getItem() instanceof TorchItem) {
            int fuel = TorchItem.getFuel(itemStack);

            if (fuel == 0) {
                ((FuelBlockEntity) be).setFuel(Mod.config.defaultTorchFuel);
            } else {
                ((FuelBlockEntity) be).setFuel(fuel);
            }
        }
    }
    // endregion

    // region IFuelBlock
    @Override
    public void outOfFuel(World world, BlockPos pos, BlockState state, boolean playSound) {
        burnOut(world, pos, state, playSound);
    }
    // endregion
}
