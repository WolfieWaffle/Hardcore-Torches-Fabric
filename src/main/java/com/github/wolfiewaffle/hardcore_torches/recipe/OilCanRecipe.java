package com.github.wolfiewaffle.hardcore_torches.recipe;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.item.OilCanItem;
import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class OilCanRecipe extends ShapelessRecipe {
    final int fuelAmount;

    public OilCanRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input, int fuelAmount) {
        super(id, group, output, input);
        this.fuelAmount = fuelAmount;
    }

    public RecipeSerializer<?> getSerializer() {
        return new Serializer();
    }

    @Override
    public ItemStack craft(CraftingInventory grid) {
        int startFuel;

        for(int i = 0; i < grid.size(); ++i) {
            ItemStack itemstack = grid.getStack(i);

            if (itemstack.getItem() instanceof OilCanItem) {
                OilCanItem can = (OilCanItem) itemstack.getItem();

                startFuel = can.getFuel(itemstack);

                return OilCanItem.setFuel(itemstack.copy(), startFuel + (fuelAmount * Mod.config.oilRecipeMultiplier));
            }
        }

        return ItemStack.EMPTY;
    }

    public static class Serializer implements RecipeSerializer<OilCanRecipe> {
        public Serializer() {
        }

        private static final Identifier NAME = new Identifier("hardcore_torches", "oil_can");

        @Override
        public OilCanRecipe read(Identifier resourceLocation, JsonObject json) {
            ShapelessRecipe recipe = ShapelessRecipe.Serializer.SHAPELESS.read(resourceLocation, json);
            int fuel = json.get("fuel").getAsInt();

            return new OilCanRecipe(recipe.getId(), recipe.getGroup(), recipe.getOutput(), recipe.getIngredients(), fuel);
        }

        @Override
        public OilCanRecipe read(Identifier resourceLocation, PacketByteBuf friendlyByteBuf) {
            ShapelessRecipe recipe = ShapelessRecipe.Serializer.SHAPELESS.read(resourceLocation, friendlyByteBuf);
            int fuelValue = friendlyByteBuf.readVarInt();

            return new OilCanRecipe(recipe.getId(), recipe.getGroup(), recipe.getOutput(), recipe.getIngredients(), fuelValue);
        }

        @Override
        public void write(PacketByteBuf friendlyByteBuf, OilCanRecipe oilCanRecipe) {
            ShapelessRecipe rec = new ShapelessRecipe(oilCanRecipe.getId(), oilCanRecipe.getGroup(), oilCanRecipe.getOutput(), oilCanRecipe.getIngredients());
            ShapelessRecipe.Serializer.SHAPELESS.write(friendlyByteBuf, rec);

            friendlyByteBuf.writeVarInt(oilCanRecipe.fuelAmount);
        }
    }
}
