package com.github.wolfiewaffle.hardcore_torches.mixin;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.item.LanternItem;
import com.github.wolfiewaffle.hardcore_torches.item.TorchItem;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class InventoryTickMixin {

    @Shadow public abstract World getWorld();

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo info) {
        if (!getWorld().isClient) {

            PlayerInventory inventory = ((ServerPlayerEntity) (Object) this).getInventory(); //idek man, Commoble told me to do it

            for (int i = 0; i < inventory.offHand.size(); i++) {
                tickTorch(inventory.offHand.get(i), inventory, i, inventory.offHand);
            }

            for (int i = 0; i < inventory.main.size(); i++) {
                tickTorch(inventory.main.get(i), inventory, i, inventory.main);
            }
        }
    }

    private void tickTorch(ItemStack stack, PlayerInventory inventory, int index, DefaultedList<ItemStack> list) {
        Item item = stack.getItem();

        if (item instanceof LanternItem && ((LanternItem) item).isLit) {
            if (Mod.config.tickInInventory) list.set(index, LanternItem.addFuel(stack, getWorld(),-1));
        }

        if (item instanceof TorchItem && ((TorchItem) item).getTorchState() == ETorchState.LIT) {
            if (Mod.config.tickInInventory) list.set(index, TorchItem.addFuel(stack, getWorld(),-1));
        }
    }
}
