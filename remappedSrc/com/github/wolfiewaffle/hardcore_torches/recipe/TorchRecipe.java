package com.github.wolfiewaffle.hardcore_torches.recipe;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.item.TorchItem;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;

public class TorchRecipe extends ShapedRecipe {

    public TorchRecipe(Identifier id, String group, int width, int height, DefaultedList<Ingredient> input, ItemStack output) {
        super(id, group, CraftingRecipeCategory.EQUIPMENT, width, height, input, output);
    }

    public RecipeSerializer<?> getSerializer() {
        return Mod.TORCH_RECIPE_SERIALIZER;
    }

    @Override
    public ItemStack craft(RecipeInputInventory grid, DynamicRegistryManager dynamicRegistryManager) {
        ItemStack stack = getOutput(dynamicRegistryManager);
        Item stackItem;

        if (stack.getItem() instanceof TorchItem && Mod.config.craftUnlit) {
            stackItem = ((TorchItem) stack.getItem()).getTorchGroup().getStandingTorch(ETorchState.UNLIT).asItem();
        } else {
            stackItem = stack.getItem();
        }

        return new ItemStack(stackItem, Mod.config.craftAmount);
    }

    public static class Serializer implements RecipeSerializer<TorchRecipe> {
        public Serializer() {
        }

        private static final Identifier NAME = new Identifier("hardcore_torches", "torch");

        @Override
        public TorchRecipe read(Identifier resourceLocation, JsonObject json) {
            ShapedRecipe recipe = ShapedRecipe.Serializer.SHAPED.read(resourceLocation, json);

            return new TorchRecipe(recipe.getId(), recipe.getGroup(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getOutput(null));
        }

        @Override
        public TorchRecipe read(Identifier resourceLocation, PacketByteBuf friendlyByteBuf) {
            ShapedRecipe recipe = ShapedRecipe.Serializer.SHAPED.read(resourceLocation, friendlyByteBuf);

            return new TorchRecipe(recipe.getId(), recipe.getGroup(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getOutput(null));
        }

        @Override
        public void write(PacketByteBuf friendlyByteBuf, TorchRecipe torchRecipe) {
            ShapedRecipe rec = new ShapedRecipe(torchRecipe.getId(), torchRecipe.getGroup(), CraftingRecipeCategory.EQUIPMENT, torchRecipe.getWidth(), torchRecipe.getHeight(), torchRecipe.getIngredients(), torchRecipe.getOutput(null));

            ShapedRecipe.Serializer.SHAPED.write(friendlyByteBuf, rec);
        }
    }
}
