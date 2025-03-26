//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

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

  public LootFunctionType getType() {
    return Mod.HARDCORE_TORCH_LOOT_FUNCTION;
  }

  protected ItemStack process(ItemStack stack, LootContext context) {
    BlockState state = (BlockState)context.get(LootContextParameters.BLOCK_STATE);
    ItemStack itemStack = new ItemStack(state.getBlock().asItem());
    if (state.getBlock() instanceof AbstractHardcoreTorchBlock) {
      ETorchState torchState = ((AbstractHardcoreTorchBlock)state.getBlock()).burnState;
      ETorchState dropTorchState = torchState;
      if (Mod.config.torchesBurnWhenDropped) {
        if (torchState != ETorchState.BURNT) {
          dropTorchState = ETorchState.BURNT;
        }
      } else if (Mod.config.torchesExtinguishWhenBroken && torchState != ETorchState.BURNT) {
        dropTorchState = ETorchState.UNLIT;
      }

      if (dropTorchState == ETorchState.SMOLDERING) {
        dropTorchState = ETorchState.UNLIT;
      }

      if (Mod.config.burntStick && dropTorchState == ETorchState.BURNT) {
        itemStack = new ItemStack(Items.STICK);
      } else {
        itemStack = this.getChangedStack(state, dropTorchState);
      }
    }

    return itemStack;
  }

  private ItemStack getChangedStack(BlockState state, ETorchState torchState) {
    return new ItemStack(((AbstractHardcoreTorchBlock)state.getBlock()).group.getStandingTorch(torchState).asItem());
  }

  public static class Serializer extends ConditionalLootFunction.Serializer<TorchLootFunction> {
    public Serializer() {
    }

    public TorchLootFunction fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
      return new TorchLootFunction(lootConditions);
    }
  }
}
