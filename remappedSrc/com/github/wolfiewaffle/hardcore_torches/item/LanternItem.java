package com.github.wolfiewaffle.hardcore_torches.item;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LanternItem extends BlockItem implements FabricItem {
    int maxFuel;
    public boolean isLit;

    public LanternItem(Block block, Settings settings, int maxFuel, boolean isLit) {
        super(block, settings);
        this.maxFuel = maxFuel;
        this.isLit = isLit;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        int fuel = getFuel(stack);

        if (fuel < maxFuel) {
            return true;
        }

        return false;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        int fuel = getFuel(stack);

        if (maxFuel != 0) {
            return Math.round(13.0f - (maxFuel - fuel) * 13.0f / maxFuel);
        }

        return 0;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return MathHelper.hsvToRgb(3.0f, 1.0f, 1.0f);
    }

    public static int getFuel(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();

        if (nbt != null) {
            return nbt.getInt("Fuel");
        } else {
            return ((LanternItem) stack.getItem()).isLit ? Mod.config.defaultLanternFuel : Mod.config.startingLanternFuel;
        }
    }

    @Override
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

        if (oldNbt == null && newNbt != null) return true;
        if (oldNbt != null && newNbt == null) return true;
        if (oldNbt == null && newNbt == null) return false;

        return oldNbt.equals(null);
    }

    public static ItemStack addFuel(ItemStack stack, World world, int amount) {

        if (stack.getItem() instanceof LanternItem && !world.isClient) {
            LanternItem item = (LanternItem) stack.getItem();

            NbtCompound nbt = stack.getNbt();
            int fuel = item.isLit ? item.maxFuel : 0;

            if (nbt != null) {
                fuel = nbt.getInt("Fuel");
            } else {
                nbt = new NbtCompound();
            }

            fuel += amount;

            // If burn out
            if (fuel <= 0) {
                stack = stateStack(stack, false);
            } else {
                if (fuel > Mod.config.defaultLanternFuel) {
                    fuel = Mod.config.defaultLanternFuel;
                }

                nbt.putInt("Fuel", fuel);
                stack.setNbt(nbt);
            }
        }

        return stack;
    }

    public static ItemStack stateStack(ItemStack inputStack, boolean isLit) {
        ItemStack outputStack = ItemStack.EMPTY;

        if (inputStack.getItem() instanceof BlockItem && inputStack.getItem() instanceof LanternItem) {
            LanternItem newItem = (LanternItem) (isLit ? Mod.LIT_LANTERN.asItem() : Mod.UNLIT_LANTERN.asItem());

            outputStack = new ItemStack(newItem, inputStack.getCount());

            if (inputStack.getNbt() != null) {
                outputStack.setNbt(inputStack.getNbt().copy());
            }
        }

        return outputStack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Mod.config.lanternsNeedCan) tooltip.add(Text.of("Requires an Oil Can").getWithStyle(Style.EMPTY.withColor(Formatting.GRAY)).get(0));
        tooltip.add(Text.of("Light with Flint and Steel").getWithStyle(Style.EMPTY.withColor(Formatting.GRAY)).get(0));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
