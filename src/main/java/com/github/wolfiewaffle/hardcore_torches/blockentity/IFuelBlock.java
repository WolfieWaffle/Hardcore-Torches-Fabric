package com.github.wolfiewaffle.hardcore_torches.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IFuelBlock {

    public void outOfFuel(World world, BlockPos pos, BlockState state);

    default boolean itemValid(ItemStack stack, Tag free, Tag damage, Tag consume) {

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

    default boolean attemptUse(ItemStack stack, PlayerEntity player, Hand hand, Tag free, Tag damage, Tag consume) {

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
}
