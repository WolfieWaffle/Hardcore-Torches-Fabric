package com.github.wolfiewaffle.hardcore_torches.loot;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.block.AbstractHardcoreTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;

public class TorchLootFunction extends ConditionalLootFunction {

    protected TorchLootFunction(LootCondition[] conditions) {
        super(conditions);
    }

    @Override
    public LootFunctionType getType() {
        return Mod.HARDCORE_TORCH_LOOT_FUNCTION;
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        BlockState state = context.get(LootContextParameters.BLOCK_STATE);
        ItemStack itemStack = new ItemStack(state.getBlock().asItem());
        ETorchState torchState;
        ETorchState dropTorchState;

        // Non-fuel modifications
        if (state.getBlock() instanceof AbstractHardcoreTorchBlock) {
            torchState = ((AbstractHardcoreTorchBlock) state.getBlock()).burnState;
            dropTorchState = torchState;

            // If torches burn out when dropped
            if (Mod.config.torchesBurnWhenDropped) {
                if (dropTorchState != ETorchState.BURNT) {
                    dropTorchState = ETorchState.BURNT;
                }
            } else {
                // If torches extinguish when dropped
                if (Mod.config.torchesExtinguishWhenBroken) {
                    if (dropTorchState != ETorchState.BURNT) {
                        dropTorchState = ETorchState.UNLIT;
                    }
                }
            }

            // If smoldering, drop unlit
            if (dropTorchState == ETorchState.SMOLDERING) {
                dropTorchState = ETorchState.UNLIT;
            }

            // Set item stack
            if (Mod.config.burntStick && dropTorchState == ETorchState.BURNT) {
                // If burnt torches drop sticks
                itemStack = new ItemStack(Items.STICK);
            } else {
                itemStack = getChangedStack(state, dropTorchState);
            }
        }

        return itemStack;
    }

    private ItemStack getChangedStack(BlockState state, ETorchState torchState) {
        return new ItemStack(((AbstractHardcoreTorchBlock) state.getBlock()).group.getStandingTorch(torchState).asItem());
    }

    public static class Serializer extends ConditionalLootFunction.Serializer<TorchLootFunction> {

        @Override
        public TorchLootFunction fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
            return new TorchLootFunction(lootConditions);
        }
    }
}
