package com.github.wolfiewaffle.hardcore_torches.loot;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.blockentity.FuelBlockEntity;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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
        BlockEntity blockEntity = context.get(LootContextParameters.BLOCK_ENTITY);
        BlockState state = context.get(LootContextParameters.BLOCK_STATE);
        int remainingFuel = Integer.MAX_VALUE;
        ItemStack itemStack = new ItemStack(state.getBlock().asItem());

        // Set fuel
        if (blockEntity != null && blockEntity instanceof FuelBlockEntity) {
            remainingFuel = ((FuelBlockEntity) blockEntity).getFuel();
            NbtCompound nbt = new NbtCompound();
            nbt.putInt("Fuel", (remainingFuel));
            itemStack.setNbt(nbt);
        }

        return itemStack;
    }

    public static class Serializer extends ConditionalLootFunction.Serializer<LanternLootFunction> {

        @Override
        public LanternLootFunction fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
            return new LanternLootFunction(lootConditions);
        }
    }
}
