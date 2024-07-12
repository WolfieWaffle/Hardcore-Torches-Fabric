package com.github.wolfiewaffle.hardcore_torches.recipe;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.item.TorchItem;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.dynamic.Codecs;

public class TorchRecipe extends ShapedRecipe {
    private final RawShapedRecipe raw;
    private final ItemStack result;

    public TorchRecipe(String group, RawShapedRecipe raw, ItemStack result) {
        super(group, CraftingRecipeCategory.EQUIPMENT, raw, result, false);
        this.raw = raw;
        this.result = result;
    }

    public RecipeSerializer<?> getSerializer() {
        return Mod.TORCH_RECIPE_SERIALIZER;
    }

    @Override
    public ItemStack craft(RecipeInputInventory grid, DynamicRegistryManager dynamicRegistryManager) {
        ItemStack stack = getResult(dynamicRegistryManager);
        Item stackItem;

        if (stack.getItem() instanceof TorchItem && Mod.config.craftUnlit) {
            stackItem = ((TorchItem) stack.getItem()).getTorchGroup().getStandingTorch(ETorchState.UNLIT).asItem();
        } else {
            stackItem = stack.getItem();
        }

        return new ItemStack(stackItem, Mod.config.craftAmount);
    }

    public static class Serializer implements RecipeSerializer<TorchRecipe> {

        public static final Codec<TorchRecipe> CODEC = RecordCodecBuilder.create((instance) -> instance.group(

                Codecs.createStrictOptionalFieldCodec(Codec.STRING, "group", "").forGetter((recipe) -> recipe.getGroup()),

                RawShapedRecipe.CODEC.forGetter((recipe) -> recipe.raw),

                ItemStack.RECIPE_RESULT_CODEC.fieldOf("result").forGetter((recipe) -> recipe.result)

        ).apply(instance, TorchRecipe::new));

        public Serializer() {
        }

        @Override
        public Codec<TorchRecipe> codec() {
            return CODEC;
        }

        @Override
        public TorchRecipe read(PacketByteBuf buf) {
            return null;
        }

        @Override
        public void write(PacketByteBuf buf, TorchRecipe recipe) {

        }
    }
}
