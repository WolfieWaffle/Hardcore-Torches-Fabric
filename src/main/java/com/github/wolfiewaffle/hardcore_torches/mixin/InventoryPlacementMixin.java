package com.github.wolfiewaffle.hardcore_torches.mixin;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.item.TorchItem;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LootableContainerBlockEntity.class)
public abstract class InventoryPlacementMixin {

    @Shadow
    protected abstract DefaultedList<ItemStack> method_11282();

    @Inject(at = @At("TAIL"), method = "setStack")
    private void setStack(int var1, ItemStack var2, CallbackInfo info) {
        if (Mod.config.unlightInChest) {
            Item item = var2.getItem();

            if (item instanceof TorchItem) {
                if (((TorchItem) item).getTorchState() == ETorchState.LIT || ((TorchItem) item).getTorchState() == ETorchState.SMOLDERING) {
                    this.method_11282().set(var1, TorchItem.stateStack(var2, ETorchState.UNLIT));
                }
            }
        }
    }
}
