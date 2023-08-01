package com.github.wolfiewaffle.hardcore_torches.compat;

import com.github.wolfiewaffle.hardcore_torches.item.LanternItem;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class LanternTrinket implements Trinket {

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {

        if (stack.getItem() instanceof LanternItem) {
            ItemStack newStack = LanternItem.addFuel(stack, entity.getWorld(), -1);
            slot.inventory().setStack(slot.index(), newStack);
        }
    }
}
