package com.github.wolfiewaffle.hardcore_torches.item;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.block.AbstractHardcoreTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.config.HardcoreTorchesConfig;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TorchItem extends WallStandingBlockItem {
    ETorchState torchState;
    int maxFuel;

    public TorchItem(Block standingBlock, Block wallBlock, Settings settings, ETorchState torchState, int maxFuel) {
        super(standingBlock, wallBlock, settings);
        this.torchState = torchState;
        this.maxFuel = maxFuel;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        int fuel = getFuel(stack);

        if (fuel > 0 && fuel < maxFuel) {
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
        //return Math.round(13.0f - (float)stack.getDamage() * 13.0f / (float)this.maxDamage);
        return 0;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return MathHelper.hsvToRgb(3.0f, 1.0f, 1.0f);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack cStack = context.getStack();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();

        // Make sure it's a torch and get its type
        if (cStack.getItem() instanceof TorchItem) {
            ETorchState torchState = ((TorchItem) cStack.getItem()).torchState;
            Block block = world.getBlockState(pos).getBlock();

            if (torchState == ETorchState.UNLIT || torchState == ETorchState.SMOLDERING) {

                // Unlit and Smoldering
                if (HardcoreTorchesConfig.getBlocks(Mod.config.worldLightBlocks).contains(block)) {
                    PlayerEntity player = context.getPlayer();
                    if (player != null && !world.isClient)
                        player.setStackInHand(context.getHand(), litStack(cStack));
                    if (!world.isClient) world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 0.5f, 1.2f);
                    return ActionResult.SUCCESS;
                }
            }
        }

        return super.useOnBlock(context);
    }

    public static ItemStack litStack(ItemStack inputStack) {
        ItemStack outputStack = ItemStack.EMPTY;

        if (inputStack.getItem() instanceof BlockItem && inputStack.getItem() instanceof TorchItem) {
            AbstractHardcoreTorchBlock newBlock = (AbstractHardcoreTorchBlock) ((BlockItem)inputStack.getItem()).getBlock();
            TorchItem newItem = (TorchItem) newBlock.group.getStandingTorch(ETorchState.LIT).asItem();

            outputStack = changedCopy(inputStack, newItem);
        }

        return outputStack;
    }

    public static int getFuel(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        int fuel;

        if (nbt != null) {
            return nbt.getInt("Fuel");
        }

        return 0;
    }

    public static ItemStack changedCopy(ItemStack stack, Item replacementItem) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack itemStack = new ItemStack(replacementItem, stack.getCount());
        itemStack.setBobbingAnimationTime(stack.getBobbingAnimationTime());
        if (stack.getNbt() != null) {
            itemStack.setNbt(stack.getNbt().copy());
        }
        return itemStack;
    }
}
