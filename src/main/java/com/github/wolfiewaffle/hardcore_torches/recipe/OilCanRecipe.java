package com.github.wolfiewaffle.hardcore_torches.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class OilCanRecipe extends ShapelessRecipe {
    final int fuelAmount;

    public OilCanRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input, int fuelAmount) {
        super(id, group, output, input);
        this.fuelAmount = fuelAmount;
    }
}
