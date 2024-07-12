package com.github.wolfiewaffle.hardcore_torches.recipe;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.item.OilCanItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class OilCanRecipe extends ShapelessRecipe {
    final int fuelAmount;
    final int configType;
    final ItemStack result;

    public OilCanRecipe(String group, ItemStack output, DefaultedList<Ingredient> input, int fuelAmount, int configType) {
        super(group, CraftingRecipeCategory.EQUIPMENT, output, input);
        this.fuelAmount = fuelAmount;
        this.configType = configType;
        this.result = output;
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

        private static final Codec<OilCanRecipe> CODEC = RecordCodecBuilder.create((instance) -> instance.group(

                Codecs.createStrictOptionalFieldCodec(Codec.STRING, "group", "").forGetter((recipe) -> recipe.getGroup()),

                ItemStack.RECIPE_RESULT_CODEC.fieldOf("result").forGetter((recipe) -> recipe.result),

                Ingredient.DISALLOW_EMPTY_CODEC.listOf().fieldOf("ingredients").flatXmap((ingredients) -> {
                    Ingredient[] ingredients2 = ingredients.stream().filter((ingredient) -> !ingredient.isEmpty()).toArray((i) -> new Ingredient[i]);
                    if (ingredients2.length == 0) {
                        return DataResult.error(() -> "No ingredients for shapeless recipe");
                    } else {
                        return ingredients2.length > 9 ? DataResult.error(() -> "Too many ingredients for shapeless recipe") : DataResult.success(DefaultedList.copyOf(Ingredient.EMPTY, ingredients2));
                    }

                }, DataResult::success).forGetter((recipe) -> recipe.getIngredients()),

                Codec.INT.fieldOf("fuel").forGetter((recipe) -> recipe.fuelAmount),

                Codec.INT.fieldOf("config_type").forGetter((recipe) -> recipe.configType)

        ).apply(instance, OilCanRecipe::new));

        public Serializer() {
        }

        @Override
        public Codec<OilCanRecipe> codec() {
            return CODEC;
        }

        @Override
        public OilCanRecipe read(PacketByteBuf buf) {
            String group = buf.readString();

            int i = buf.readVarInt();
            DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(i, Ingredient.EMPTY);

            for(int j = 0; j < ingredients.size(); ++j) {
                ingredients.set(j, Ingredient.fromPacket(buf));
            }

            ItemStack itemStack = buf.readItemStack();

            int fuelAmount = buf.readInt();

            int configType = buf.readInt();

            return new OilCanRecipe(group, itemStack, ingredients, fuelAmount, configType);
        }

        @Override
        public void write(PacketByteBuf buf, OilCanRecipe oilCanRecipe) {
            buf.writeString(oilCanRecipe.getGroup());

            buf.writeVarInt(oilCanRecipe.getIngredients().size());
            Iterator var3 = oilCanRecipe.getIngredients().iterator();

            while(var3.hasNext()) {
                Ingredient ingredient = (Ingredient)var3.next();
                ingredient.write(buf);
            }

            buf.writeItemStack(oilCanRecipe.result);

            buf.writeVarInt(oilCanRecipe.fuelAmount);

            buf.writeVarInt(oilCanRecipe.configType);
        }
    }
}
