package com.github.wolfiewaffle.hardcore_torches.recipe;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.item.OilCanItem;
import com.google.gson.JsonObject;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class OilCanRecipe extends ShapelessRecipe {
    final int fuelAmount;
    final int configType;

    public OilCanRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input, int fuelAmount, int configType) {
        super(id, group, CraftingRecipeCategory.EQUIPMENT, output, input);
        this.fuelAmount = fuelAmount;
        this.configType = configType;
    }

    public RecipeSerializer<?> getSerializer() {
        return Mod.OIL_RECIPE_SERIALIZER;
    }

    @Override
    public boolean matches(RecipeInputInventory grid, World world) {
        if (configType == 0 && !Mod.config.enableFatOil) return false;
        if (configType == 1 && !Mod.config.enableCoalOil) return false;

        RecipeMatcher recipeMatcher = new RecipeMatcher();
        Item fuelItem = null;
        int i = 0;

        for(int j = 0; j < grid.size(); ++j) {
            ItemStack itemStack = grid.getStack(j);
            if (!itemStack.isEmpty()) {
                if (itemStack.getItem() instanceof OilCanItem) {
                    // Oil can
                    recipeMatcher.addInput(itemStack, 1);
                    ++i;
                } else {
                    // Anything else
                    if (fuelItem == null) {
                        recipeMatcher.addInput(itemStack, 1);
                        ++i;
                        fuelItem = itemStack.getItem();
                    } else if (fuelItem != itemStack.getItem()) {
                        recipeMatcher.addInput(itemStack, 1);
                        ++i;
                    }
                }
            }
        }

        boolean match = recipeMatcher.match(this, null);
        return i == this.getIngredients().size() && match;
    }

    @Override
    public ItemStack craft(RecipeInputInventory grid, DynamicRegistryManager dynamicRegistryManager) {
        int startFuel = 0;
        int addFuel = 0;
        ItemStack resultStack = ItemStack.EMPTY;

        for(int i = 0; i < grid.size(); ++i) {
            ItemStack itemstack = grid.getStack(i);

            if (!itemstack.isEmpty()) {
                if (itemstack.getItem() instanceof OilCanItem) {
                    OilCanItem can = (OilCanItem) itemstack.getItem();
                    startFuel = can.getFuel(itemstack);
                    resultStack = itemstack;
                } else {
                    if (Mod.config.oilRecipeOverride > -1) {
                        addFuel += Mod.config.oilRecipeOverride;
                    } else {
                        addFuel += fuelAmount * Mod.config.oilRecipeMultiplier;
                    }
                }
            }
        }

        if (resultStack.getItem() instanceof OilCanItem) {
            return OilCanItem.setFuel(resultStack.copy(), startFuel + addFuel);
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
            int configType = json.get("config_type").getAsInt();

            return new OilCanRecipe(recipe.getId(), recipe.getGroup(), recipe.getOutput(null), recipe.getIngredients(), fuel, configType);
        }

        @Override
        public OilCanRecipe read(Identifier resourceLocation, PacketByteBuf friendlyByteBuf) {
            ShapelessRecipe recipe = ShapelessRecipe.Serializer.SHAPELESS.read(resourceLocation, friendlyByteBuf);
            int fuelValue = friendlyByteBuf.readVarInt();
            int configType = friendlyByteBuf.readVarInt();

            return new OilCanRecipe(recipe.getId(), recipe.getGroup(), recipe.getOutput(null), recipe.getIngredients(), fuelValue, configType);
        }

        @Override
        public void write(PacketByteBuf friendlyByteBuf, OilCanRecipe oilCanRecipe) {
            ShapelessRecipe rec = new ShapelessRecipe(oilCanRecipe.getId(), oilCanRecipe.getGroup(), CraftingRecipeCategory.EQUIPMENT, oilCanRecipe.getOutput(null), oilCanRecipe.getIngredients());
            ShapelessRecipe.Serializer.SHAPELESS.write(friendlyByteBuf, rec);

            friendlyByteBuf.writeVarInt(oilCanRecipe.fuelAmount);
            friendlyByteBuf.writeVarInt(oilCanRecipe.configType);
        }
    }
}
