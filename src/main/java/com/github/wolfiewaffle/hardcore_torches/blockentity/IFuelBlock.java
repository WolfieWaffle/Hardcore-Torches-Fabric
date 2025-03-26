package com.github.wolfiewaffle.hardcore_torches.blockentity;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IFuelBlock {

  void outOfFuel(World world, BlockPos pos, BlockState state, boolean playSound);

  default boolean itemValid(ItemStack stack, TagKey free, TagKey damage, TagKey consume) {

    // Infinite items
    if (stack.isIn(free)) {
      return true;
    }

    // Durability items
    if (stack.isIn(damage)) {
      return true;
    }

    // Consume items
    if (stack.isIn(consume)) {
      return true;
    }

    return false;
  }

  default boolean attemptUse(ItemStack stack, PlayerEntity player, Hand hand, TagKey free, TagKey damage, TagKey consume) {

    // Infinite items
    if (stack.isIn(free)) {
      return true;
    }

    // Durability items
    if (stack.isIn(damage)) {
      if (stack.isDamageable()) {
        stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
      }
      return true;
    }

    // Consume items
    if (stack.isIn(consume)) {
      if (!player.isCreative()) {
        stack.decrement(1);
      }
      return true;
    }

    return false;
  }

  default boolean attemptUseItem(ItemStack stack, PlayerEntity player, Hand hand, ETorchState attemptedState) {
    switch (attemptedState) {
      case LIT:
        return attemptUse(stack, player, hand, getFreeLightItems(), getDamageLightItems(), getConsumeLightItems());
      case SMOLDERING:
        return attemptUse(stack, player, hand, getFreeSmotherItems(), getDamageSmotherItems(), getConsumeSmotherItems());
      case UNLIT:
        return attemptUse(stack, player, hand, getFreeExtinguishItems(), getDamageExtinguishItems(), getConsumeExtinguishItems());
    }
    return false;
  }

  default TagKey getFreeLightItems() {
    return Mod.FREE_TORCH_LIGHT_ITEMS;
  }

  default TagKey getDamageLightItems() {
    return Mod.DAMAGE_TORCH_LIGHT_ITEMS;
  }

  default TagKey getConsumeLightItems() {
    return Mod.CONSUME_TORCH_LIGHT_ITEMS;
  }

  default TagKey getFreeExtinguishItems() {
    return Mod.FREE_TORCH_EXTINGUISH_ITEMS;
  }

  default TagKey getDamageExtinguishItems() {
    return Mod.DAMAGE_TORCH_EXTINGUISH_ITEMS;
  }

  default TagKey getConsumeExtinguishItems() {
    return Mod.CONSUME_TORCH_EXTINGUISH_ITEMS;
  }

  default TagKey getFreeSmotherItems() {
    return Mod.FREE_TORCH_SMOTHER_ITEMS;
  }

  default TagKey getDamageSmotherItems() {
    return Mod.DAMAGE_TORCH_SMOTHER_ITEMS;
  }

  default TagKey getConsumeSmotherItems() {
    return Mod.CONSUME_TORCH_SMOTHER_ITEMS;
  }
}
