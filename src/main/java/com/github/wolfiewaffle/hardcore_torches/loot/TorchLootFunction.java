package com.github.wolfiewaffle.hardcore_torches.loot;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.block.AbstractHardcoreTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;

import java.util.List;

public class TorchLootFunction extends ConditionalLootFunction {

    protected TorchLootFunction(List<LootCondition> conditions) {
        super(conditions);
    }

    @Override
    public LootFunctionType getType() {
        return Mod.HARDCORE_TORCH_LOOT_FUNCTION;
    }

    public static final Codec<TorchLootFunction> CODEC = RecordCodecBuilder.create((instance) -> addConditionsField(instance).apply(instance, TorchLootFunction::new));

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        BlockState state = context.get(LootContextParameters.BLOCK_STATE);
        assert state != null;
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
}
