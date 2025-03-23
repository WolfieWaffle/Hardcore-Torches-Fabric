package com.github.wolfiewaffle.hardcore_torches.recipe;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.google.gson.JsonObject;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class CampfireRecipe extends ShapedRecipe {

    public CampfireRecipe(Identifier id, String group, int width, int height, DefaultedList<Ingredient> input, ItemStack output) {
        super(id, group, CraftingRecipeCategory.EQUIPMENT, width, height, input, output);
    }

    public RecipeSerializer<?> getSerializer() {
        return Mod.CAMPFIRE_RECIPE_SERIALIZER;
    }

    @Override
    public ItemStack craft(RecipeInputInventory grid, DynamicRegistryManager dynamicRegistryManager) {
        if (Mod.config.craftHardcoreCampfire) {
            return new ItemStack(Mod.CAMPFIRE_ITEM, 1);
        } else {
            return new ItemStack(Items.CAMPFIRE, 1);
        }
    }

    public static class Serializer implements RecipeSerializer<CampfireRecipe> {
        public Serializer() {
        }

        private static final Identifier NAME = new Identifier("hardcore_torches", "torch");

        @Override
        public CampfireRecipe read(Identifier resourceLocation, JsonObject json) {
            ShapedRecipe recipe = ShapedRecipe.Serializer.SHAPED.read(resourceLocation, json);

            return new CampfireRecipe(recipe.getId(), recipe.getGroup(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getOutput(null));
        }

        @Override
        public CampfireRecipe read(Identifier resourceLocation, PacketByteBuf friendlyByteBuf) {
            ShapedRecipe recipe = ShapedRecipe.Serializer.SHAPED.read(resourceLocation, friendlyByteBuf);

            return new CampfireRecipe(recipe.getId(), recipe.getGroup(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getOutput(null));
        }

        @Override
        public void write(PacketByteBuf friendlyByteBuf, CampfireRecipe campfireRecipe) {
            ShapedRecipe rec = new ShapedRecipe(campfireRecipe.getId(), campfireRecipe.getGroup(), CraftingRecipeCategory.EQUIPMENT, campfireRecipe.getWidth(), campfireRecipe.getHeight(), campfireRecipe.getIngredients(), campfireRecipe.getOutput(null));

            ShapedRecipe.Serializer.SHAPED.write(friendlyByteBuf, rec);
        }
    }
}
