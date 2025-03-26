//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.mixin;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.item.LanternItem;
import com.github.wolfiewaffle.hardcore_torches.item.TorchItem;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import com.mojang.authlib.GameProfile;
import java.util.Random;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ServerPlayerEntity.class})
public abstract class InventoryTickMixin extends PlayerEntity {
  private static Random random = new Random();

  public InventoryTickMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
    super(world, pos, yaw, gameProfile);
  }

  @Inject(
          at = {@At("TAIL")},
          method = {"tick"}
  )
  private void tick(CallbackInfo info) {
    if (!this.getWorld().isClient) {
      ServerPlayerEntity player = ((ServerPlayerEntity) (Object) this);
      PlayerInventory inventory = player.getInventory();

      for(int i = 0; i < inventory.offHand.size(); ++i) {
        this.tickTorch((ItemStack)inventory.offHand.get(i), inventory, i, inventory.offHand);
      }

      for(int i = 0; i < inventory.main.size(); ++i) {
        this.tickTorch((ItemStack)inventory.main.get(i), inventory, i, inventory.main);
      }

      this.waterCheck(player, inventory);
    }

  }

  private void waterCheck(ServerPlayerEntity player, PlayerInventory inventory) {
    BlockPos pos = player.getBlockPos();
    int rainEffect = Mod.config.invExtinguishInRain;
    boolean doRain = rainEffect > 0 && player.getWorld().hasRain(pos);

    for(int i = 0; i < inventory.size(); ++i) {
      ItemStack stack = inventory.getStack(i);
      Item item = stack.getItem();
      if (item instanceof TorchItem torchItem) {
        boolean rain = doRain;
        boolean mainOrOffhand = i == inventory.selectedSlot || inventory.offHand.get(0) == stack;
        if (rainEffect == 1 && doRain) {
          rain = mainOrOffhand;
        }

        if (rain) {
          this.rainTorch(i, torchItem, stack, inventory, player.getWorld(), pos);
        }

        if (Mod.config.invExtinguishInWater > 0) {
          this.waterTorch(i, torchItem, stack, player, mainOrOffhand, pos);
        }
      }
    }

  }

  private void waterTorch(int i, TorchItem torchItem, ItemStack stack, ServerPlayerEntity player, boolean mainOrOffhand, BlockPos pos) {
    if (player.isSubmergedInWater() && (torchItem.getTorchState() == ETorchState.LIT || torchItem.getTorchState() == ETorchState.SMOLDERING) && (Mod.config.invExtinguishInWater == 1 && mainOrOffhand || Mod.config.invExtinguishInWater == 2)) {
      player.getInventory().setStack(i, TorchItem.stateStack(stack, ETorchState.UNLIT));
      player.getWorld().playSound((PlayerEntity)null, pos.up(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.5F, 1.0F);
    }

  }

  private void rainTorch(int i, TorchItem torchItem, ItemStack stack, PlayerInventory inventory, World world, BlockPos pos) {
    if (torchItem.getTorchState() == ETorchState.LIT) {
      if (Mod.config.torchesSmolder) {
        inventory.setStack(i, TorchItem.stateStack(stack, ETorchState.SMOLDERING));
        world.playSound((PlayerEntity)null, pos.up(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.5F, 1.0F);
      } else {
        inventory.setStack(i, TorchItem.stateStack(stack, ETorchState.UNLIT));
        world.playSound((PlayerEntity)null, pos.up(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.5F, 1.0F);
      }
    } else if (torchItem.getTorchState() == ETorchState.SMOLDERING && !Mod.config.torchesSmolder) {
      inventory.setStack(i, TorchItem.stateStack(stack, ETorchState.UNLIT));
      world.playSound((PlayerEntity)null, pos.up(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.5F, 1.0F);
    }

  }

  private void tickTorch(ItemStack stack, PlayerInventory inventory, int index, DefaultedList<ItemStack> list) {
    Item item = stack.getItem();
    if (item instanceof LanternItem && ((LanternItem)item).isLit && Mod.config.tickInInventory) {
      list.set(index, LanternItem.addFuel(stack, this.getWorld(), -1));
    }

    if (item instanceof TorchItem) {
      ETorchState state = ((TorchItem)item).getTorchState();
      if (state == ETorchState.LIT) {
        if (Mod.config.tickInInventory) {
          list.set(index, TorchItem.addFuel(stack, this.getWorld(), -1));
        }
      } else if (state == ETorchState.SMOLDERING && Mod.config.tickInInventory && random.nextInt(3) == 0) {
        list.set(index, TorchItem.addFuel(stack, this.getWorld(), -1));
      }
    }

  }
}
