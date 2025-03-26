//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.block;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.blockentity.FuelBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.blockentity.IFuelBlock;
import com.github.wolfiewaffle.hardcore_torches.blockentity.TorchBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.item.OilCanItem;
import com.github.wolfiewaffle.hardcore_torches.item.TorchItem;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import com.github.wolfiewaffle.hardcore_torches.util.TorchGroup;
import com.github.wolfiewaffle.hardcore_torches.util.TorchTools;
import java.util.function.IntSupplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.HorizontalFacingBlock;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractHardcoreTorchBlock extends BlockWithEntity implements BlockEntityProvider, IFuelBlock {
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
      world.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
      TorchTools.displayParticle(ParticleTypes.LARGE_SMOKE, state, world, pos);
      TorchTools.displayParticle(ParticleTypes.LARGE_SMOKE, state, world, pos);
      TorchTools.displayParticle(ParticleTypes.SMOKE, state, world, pos);
      TorchTools.displayParticle(ParticleTypes.SMOKE, state, world, pos);
      this.changeTorch(world, pos, state, ETorchState.SMOLDERING);
    }

  }

  public void extinguish(World world, BlockPos pos, BlockState state) {
    if (!world.isClient) {
      world.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
      TorchTools.displayParticle(ParticleTypes.LARGE_SMOKE, state, world, pos);
      TorchTools.displayParticle(ParticleTypes.LARGE_SMOKE, state, world, pos);
      TorchTools.displayParticle(ParticleTypes.SMOKE, state, world, pos);
      TorchTools.displayParticle(ParticleTypes.SMOKE, state, world, pos);
      this.changeTorch(world, pos, state, ETorchState.UNLIT);
    }

  }

  public void burnOut(World world, BlockPos pos, BlockState state, boolean playSound) {
    if (!world.isClient) {
      if (playSound) {
        world.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
      }

      TorchTools.displayParticle(ParticleTypes.LARGE_SMOKE, state, world, pos);
      TorchTools.displayParticle(ParticleTypes.LARGE_SMOKE, state, world, pos);
      TorchTools.displayParticle(ParticleTypes.SMOKE, state, world, pos);
      TorchTools.displayParticle(ParticleTypes.SMOKE, state, world, pos);
      this.changeTorch(world, pos, state, ETorchState.BURNT);
    }

  }

  public void light(World world, BlockPos pos, BlockState state) {
    if (!world.isClient) {
      world.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 0.5F, 1.2F);
      TorchTools.displayParticle(ParticleTypes.LAVA, state, world, pos);
      TorchTools.displayParticle(ParticleTypes.FLAME, state, world, pos);
      this.changeTorch(world, pos, state, ETorchState.LIT);
    }

  }

  public abstract boolean isWall();

  public ETorchState getBurnState() {
    return this.burnState;
  }

  public void changeTorch(World world, BlockPos pos, BlockState curState, ETorchState newType) {
    BlockState newState;
    if (this.isWall()) {
      newState = (BlockState)this.group.getWallTorch(newType).getDefaultState().with(HorizontalFacingBlock.FACING, (Direction)curState.get(HardcoreWallTorchBlock.FACING));
    } else {
      newState = this.group.getStandingTorch(newType).getDefaultState();
    }

    int newFuel = 0;
    if (world.getBlockEntity(pos) != null) {
      newFuel = ((FuelBlockEntity)world.getBlockEntity(pos)).getFuel();
    }

    world.setBlockState(pos, newState);
    if (world.getBlockEntity(pos) != null) {
      ((FuelBlockEntity)world.getBlockEntity(pos)).setFuel(newFuel);
    }

  }

  public static boolean isLightItem(ItemStack stack) {
    if (stack.isIn(Mod.FREE_TORCH_LIGHT_ITEMS)) {
      return true;
    } else if (stack.isIn(Mod.DAMAGE_TORCH_LIGHT_ITEMS)) {
      return true;
    } else {
      return stack.isIn(Mod.CONSUME_TORCH_LIGHT_ITEMS);
    }
  }

  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new TorchBlockEntity(pos, state);
  }

  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
  }

  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
    return checkType(type, Mod.TORCH_BLOCK_ENTITY, (world1, pos, state1, be) -> TorchBlockEntity.tick(world1, pos, state1, be));
  }

  public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    if (this.burnState == ETorchState.LIT || this.burnState == ETorchState.SMOLDERING) {
      TorchTools.displayParticle(ParticleTypes.SMOKE, state, world, pos);
    }

    if (this.burnState == ETorchState.LIT) {
      TorchTools.displayParticle(this.particle, state, world, pos);
    }

  }

  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    ItemStack stack = player.getStackInHand(hand);
    boolean success = false;
    if (this.burnState == ETorchState.LIT) {
      if (this.attemptUse(stack, player, hand, Mod.FREE_TORCH_EXTINGUISH_ITEMS, Mod.DAMAGE_TORCH_EXTINGUISH_ITEMS, Mod.CONSUME_TORCH_EXTINGUISH_ITEMS)) {
        this.extinguish(world, pos, state);
        player.swingHand(hand);
        return ActionResult.SUCCESS;
      }

      if (this.attemptUse(stack, player, hand, Mod.FREE_TORCH_SMOTHER_ITEMS, Mod.DAMAGE_TORCH_SMOTHER_ITEMS, Mod.CONSUME_TORCH_SMOTHER_ITEMS)) {
        this.smother(world, pos, state);
        player.swingHand(hand);
        return ActionResult.SUCCESS;
      }
    }

    if ((this.burnState == ETorchState.SMOLDERING || this.burnState == ETorchState.UNLIT) && this.attemptUse(stack, player, hand, Mod.FREE_TORCH_LIGHT_ITEMS, Mod.DAMAGE_TORCH_LIGHT_ITEMS, Mod.CONSUME_TORCH_LIGHT_ITEMS)) {
      this.light(world, pos, state);
      player.swingHand(hand);
      return ActionResult.SUCCESS;
    } else {
      BlockEntity be = world.getBlockEntity(pos);
      if (be.getType() == Mod.TORCH_BLOCK_ENTITY && !world.isClient && Mod.config.fuelMessage && stack.isEmpty()) {
        player.sendMessage(Text.of("Fuel: " + ((TorchBlockEntity)be).getFuel()), true);
      }

      if (Mod.config.torchesUseCan && this.burnState != ETorchState.BURNT && !world.isClient && OilCanItem.fuelBlock((FuelBlockEntity)be, world, stack)) {
        world.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
      }

      if (Mod.config.handUnlightTorch && (this.burnState == ETorchState.LIT || this.burnState == ETorchState.SMOLDERING) && !TorchTools.canLight(stack.getItem(), state)) {
        this.extinguish(world, pos, state);
        return ActionResult.SUCCESS;
      } else {
        return ActionResult.PASS;
      }
    }
  }

  public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
    super.onPlaced(world, pos, state, placer, itemStack);
    BlockEntity be = world.getBlockEntity(pos);
    if (be != null && be instanceof FuelBlockEntity && itemStack.getItem() instanceof TorchItem) {
      int fuel = TorchItem.getFuel(itemStack);
      if (fuel == 0) {
        ((FuelBlockEntity)be).setFuel(Mod.config.defaultTorchFuel);
      } else {
        ((FuelBlockEntity)be).setFuel(fuel);
      }
    }

  }

  public void outOfFuel(World world, BlockPos pos, BlockState state, boolean playSound) {
    this.burnOut(world, pos, state, playSound);
  }
}
