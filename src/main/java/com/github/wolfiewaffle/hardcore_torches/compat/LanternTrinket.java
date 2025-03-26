//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.compat;

import com.github.wolfiewaffle.hardcore_torches.item.LanternItem;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class LanternTrinket implements Trinket {
  public LanternTrinket() {
  }

  public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
    if (stack.getItem() instanceof LanternItem) {
      ItemStack newStack = LanternItem.addFuel(stack, entity.getWorld(), -1);
      slot.inventory().setStack(slot.index(), newStack);
    }

  }
}
