package com.github.wolfiewaffle.hardcore_torches.loot;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.block.AbstractHardcoreTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.block.AbstractLanternBlock;
import com.github.wolfiewaffle.hardcore_torches.blockentity.FuelBlockEntity;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.nbt.NbtCompound;

public class LanternLootFunction extends ConditionalLootFunction {

    protected LanternLootFunction(LootCondition[] conditions) {
        super(conditions);
    }

    @Override
    public LootFunctionType getType() {
        return Mod.FUEL_LOOT_FUNCTION;
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        if (!(stack.getItem() instanceof BlockItem)) return stack; // No regular items

        BlockEntity blockEntity = context.get(LootContextParameters.BLOCK_ENTITY);
        Block block = ((BlockItem) stack.getItem()).getBlock();

        if (block instanceof AbstractHardcoreTorchBlock || block instanceof AbstractLanternBlock) {

            // Set fuel
            if (blockEntity != null && blockEntity instanceof FuelBlockEntity) {
                int remainingFuel = ((FuelBlockEntity) blockEntity).getFuel();

                if (remainingFuel != Mod.config.defaultTorchFuel) {
                    NbtCompound nbt = new NbtCompound();
                    nbt.putInt("Fuel", (remainingFuel));
                    stack.setNbt(nbt);
                }
            }

            if (block instanceof AbstractHardcoreTorchBlock && ((AbstractHardcoreTorchBlock) ((BlockItem) stack.getItem()).getBlock()).burnState == ETorchState.BURNT) {
                stack.removeSubNbt("Fuel");
            }
        }

        return stack;
    }

    public static class Serializer extends ConditionalLootFunction.Serializer<LanternLootFunction> {

        @Override
        public LanternLootFunction fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
            return new LanternLootFunction(lootConditions);
        }
    }
}
