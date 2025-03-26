//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.item;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.blockentity.FuelBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.blockentity.LanternBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.blockentity.TorchBlockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class OilCanItem extends Item implements FabricItem {
  public OilCanItem(Item.Settings settings) {
    super(settings);
  }

  public boolean isItemBarVisible(ItemStack stack) {
    return true;
  }

  public int getItemBarStep(ItemStack stack) {
    int maxFuel = Mod.config.maxCanFuel;
    int fuel = getFuel(stack);
    return maxFuel != 0 ? Math.round(13.0F - (float)(maxFuel - fuel) * 13.0F / (float)maxFuel) : 0;
  }

  public int getItemBarColor(ItemStack stack) {
    return MathHelper.hsvToRgb(3.0F, 1.0F, 1.0F);
  }

  public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
    NbtCompound oldNbt = null;
    NbtCompound newNbt = null;
    if (oldStack.getNbt() != null) {
      oldNbt = oldStack.getNbt().copy();
      oldNbt.remove("Fuel");
    }

    if (newStack.getNbt() != null) {
      newNbt = newStack.getNbt().copy();
      newNbt.remove("Fuel");
    }

    if (oldNbt == null && newNbt != null) {
      return true;
    } else if (oldNbt != null && newNbt == null) {
      return true;
    } else {
      return oldNbt == null && newNbt == null ? false : oldNbt.equals((Object)null);
    }
  }

  public static int getFuel(ItemStack stack) {
    Item item = stack.getItem();
    if (!(item instanceof OilCanItem)) {
      return 0;
    } else {
      NbtCompound nbt = stack.getNbt();
      return nbt != null && nbt.contains("Fuel") ? nbt.getInt("Fuel") : 0;
    }
  }

  public static ItemStack setFuel(ItemStack stack, int fuel) {
    if (stack.getItem() instanceof OilCanItem) {
      NbtCompound nbt = stack.getNbt();
      if (nbt == null) {
        nbt = new NbtCompound();
      }

      nbt.putInt("Fuel", Math.max(0, Math.min(Mod.config.maxCanFuel, fuel)));
      stack.setNbt(nbt);
    }

    return stack;
  }

  public static ItemStack addFuel(ItemStack stack, int amount) {
    if (stack.getItem() instanceof OilCanItem) {
      NbtCompound nbt = stack.getNbt();
      int fuel = 0;
      if (nbt != null) {
        fuel = nbt.getInt("Fuel");
      } else {
        nbt = new NbtCompound();
      }

      fuel = Math.min(Mod.config.maxCanFuel, Math.max(0, fuel + amount));
      nbt.putInt("Fuel", fuel);
      stack.setNbt(nbt);
    }

    return stack;
  }

  public static boolean fuelBlock(FuelBlockEntity be, World world, ItemStack stack) {
    if (!world.isClient) {
      int maxTaken = 0;
      if (be instanceof LanternBlockEntity) {
        maxTaken = Math.max(0, Mod.config.defaultLanternFuel - be.getFuel());
      }

      if (be instanceof TorchBlockEntity) {
        maxTaken = Math.max(0, Mod.config.defaultTorchFuel - be.getFuel());
      }

      int taken = Math.min(maxTaken, getFuel(stack));
      addFuel(stack, -taken);
      be.setFuel(be.getFuel() + taken);
      return taken > 0;
    } else {
      return false;
    }
  }
}
