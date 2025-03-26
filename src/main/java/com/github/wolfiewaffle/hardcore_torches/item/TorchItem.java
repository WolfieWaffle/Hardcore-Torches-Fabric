//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.item;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.block.AbstractHardcoreTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import com.github.wolfiewaffle.hardcore_torches.util.TorchGroup;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TorchItem extends VerticallyAttachableBlockItem implements FabricItem {
  ETorchState torchState;
  TorchGroup torchGroup;
  int maxFuel;

  public TorchItem(Block standingBlock, Block wallBlock, Item.Settings settings, ETorchState torchState, int maxFuel, TorchGroup group) {
    super(standingBlock, wallBlock, settings, Direction.DOWN);
    this.torchState = torchState;
    this.maxFuel = maxFuel;
    this.torchGroup = group;
  }

  public boolean isItemBarVisible(ItemStack stack) {
    int fuel = getFuel(stack);
    return fuel > 0 && fuel < this.maxFuel;
  }

  public int getItemBarStep(ItemStack stack) {
    int fuel = getFuel(stack);
    return this.maxFuel != 0 ? Math.round(13.0F - (float)(this.maxFuel - fuel) * 13.0F / (float)this.maxFuel) : 0;
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

  public ActionResult useOnBlock(ItemUsageContext context) {
    ItemStack stack = context.getStack();
    World world = context.getWorld();
    BlockPos pos = context.getBlockPos();
    BlockState state = world.getBlockState(pos);
    if (stack.getItem() instanceof TorchItem) {
      ETorchState torchState = ((TorchItem)stack.getItem()).torchState;
      if ((torchState == ETorchState.UNLIT || torchState == ETorchState.SMOLDERING) && state.isIn(Mod.FREE_TORCH_LIGHT_BLOCKS)) {
        if (state.contains(Properties.LIT) && !(Boolean)state.get(Properties.LIT)) {
          return super.useOnBlock(context);
        }

        PlayerEntity player = context.getPlayer();
        if (player != null && !world.isClient) {
          player.setStackInHand(context.getHand(), stateStack(stack, ETorchState.LIT));
        }

        if (!world.isClient) {
          world.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 0.5F, 1.2F);
        }

        return ActionResult.SUCCESS;
      }
    }

    return super.useOnBlock(context);
  }

  public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
    if (slot.canTakePartial(player) && otherStack.getItem() instanceof TorchItem && !otherStack.isEmpty()) {
      if (clickType == ClickType.RIGHT || stack.getCount() < stack.getMaxCount() && otherStack.getCount() < otherStack.getMaxCount()) {
        if (!this.sameTorchGroup((TorchItem)stack.getItem(), (TorchItem)otherStack.getItem())) {
          return false;
        } else {
          if (((TorchItem)stack.getItem()).torchState == ETorchState.LIT) {
            if (((TorchItem)otherStack.getItem()).torchState == ETorchState.BURNT) {
              return false;
            }
          } else if (((TorchItem)stack.getItem()).torchState == ETorchState.UNLIT && ((TorchItem)otherStack.getItem()).torchState != ETorchState.UNLIT) {
            return false;
          }

          if (!otherStack.isEmpty()) {
            int max = stack.getMaxCount();
            int usedCount = clickType != ClickType.RIGHT ? otherStack.getCount() : 1;
            int otherMax = otherStack.getMaxCount();
            int remainder = Math.max(0, usedCount - (max - stack.getCount()));
            int addedNew = usedCount - remainder;
            int stack1Fuel = getFuel(stack) * stack.getCount();
            int stack2Fuel = getFuel(otherStack) * addedNew;
            int totalFuel = stack1Fuel + stack2Fuel;
            NbtCompound nbt = new NbtCompound();
            nbt.putInt("Fuel", totalFuel / (stack.getCount() + addedNew));
            if (addedNew > 0) {
              stack.increment(addedNew);
              stack.setNbt(nbt);
              otherStack.setCount(otherStack.getCount() - addedNew);
              return true;
            }
          }

          return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
        }
      } else {
        return false;
      }
    } else {
      return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }
  }

  public boolean sameTorchGroup(TorchItem item1, TorchItem item2) {
    return item1.torchGroup == item2.torchGroup;
  }

  public static Item stateItem(Item inputItem, ETorchState newState) {
    Item outputItem = Items.AIR;
    if (inputItem instanceof BlockItem && inputItem instanceof TorchItem) {
      AbstractHardcoreTorchBlock newBlock = (AbstractHardcoreTorchBlock)((BlockItem)inputItem).getBlock();
      TorchItem newItem = (TorchItem)newBlock.group.getStandingTorch(newState).asItem();
      outputItem = newItem;
    }

    return outputItem;
  }

  public static ItemStack stateStack(ItemStack inputStack, ETorchState newState) {
    ItemStack outputStack = ItemStack.EMPTY;
    if (inputStack.getItem() instanceof BlockItem && inputStack.getItem() instanceof TorchItem) {
      AbstractHardcoreTorchBlock newBlock = (AbstractHardcoreTorchBlock)((BlockItem)inputStack.getItem()).getBlock();
      TorchItem newItem = (TorchItem)newBlock.group.getStandingTorch(newState).asItem();
      outputStack = changedCopy(inputStack, newItem);
      if (newState == ETorchState.BURNT) {
        outputStack.setNbt((NbtCompound)null);
      }
    }

    return outputStack;
  }

  public static int getFuel(ItemStack stack) {
    NbtCompound nbt = stack.getNbt();
    return nbt != null ? nbt.getInt("Fuel") : Mod.config.defaultTorchFuel;
  }

  public ETorchState getTorchState() {
    return this.torchState;
  }

  public TorchGroup getTorchGroup() {
    return this.torchGroup;
  }

  public static ItemStack changedCopy(ItemStack stack, Item replacementItem) {
    if (stack.isEmpty()) {
      return ItemStack.EMPTY;
    } else {
      ItemStack itemStack = new ItemStack(replacementItem, stack.getCount());
      if (stack.getNbt() != null) {
        itemStack.setNbt(stack.getNbt().copy());
      }

      return itemStack;
    }
  }

  public static ItemStack addFuel(ItemStack stack, World world, int amount) {
    if (stack.getItem() instanceof TorchItem && !world.isClient) {
      NbtCompound nbt = stack.getNbt();
      int fuel = Mod.config.defaultTorchFuel;
      if (nbt != null) {
        fuel = nbt.getInt("Fuel");
      } else {
        nbt = new NbtCompound();
      }

      fuel += amount;
      if (fuel <= 0) {
        if (Mod.config.burntStick) {
          stack = new ItemStack(Items.STICK, stack.getCount());
        } else {
          stack = stateStack(stack, ETorchState.BURNT);
        }
      } else {
        if (fuel > Mod.config.defaultTorchFuel) {
          fuel = Mod.config.defaultTorchFuel;
        }

        nbt.putInt("Fuel", fuel);
        stack.setNbt(nbt);
      }
    }

    return stack;
  }
}
