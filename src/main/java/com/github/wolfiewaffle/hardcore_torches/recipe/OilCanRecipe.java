package com.github.wolfiewaffle.hardcore_torches.recipe;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.config.HardcoreTorchesConfig;
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
import net.minecraft.world.World;

public class OilCanRecipe extends ShapelessRecipe {
    final int fuelAmount;
    final int configType;

    public OilCanRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input, int fuelAmount, int configType) {
        super(id, group, output, input);
        this.fuelAmount = fuelAmount;
        this.configType = configType;
    }

    public RecipeSerializer<?> getSerializer() {
        return Mod.OIL_RECIPE_SERIALIZER;
    }

    @Override
    public boolean matches(CraftingInventory craftingInventory, World world) {
        if (configType == 0 && !Mod.config.enableFatOil) return false;
        if (configType == 1 && !Mod.config.enableCoalOil) return false;
        return super.matches(craftingInventory, world);
    }

    @Override
    public ItemStack craft(CraftingInventory grid) {
        int startFuel;

        for(int i = 0; i < grid.size(); ++i) {
            ItemStack itemstack = grid.getStack(i);

            if (itemstack.getItem() instanceof OilCanItem) {
                OilCanItem can = (OilCanItem) itemstack.getItem();

                startFuel = can.getFuel(itemstack);
                int fuel = (int) (fuelAmount * Mod.config.oilRecipeMultiplier);
                System.out.println("REC " + Mod.config.oilRecipeOverride);
                if (Mod.config.oilRecipeOverride > -1) {
                    fuel = Mod.config.oilRecipeOverride;
                    System.out.println("REC " + fuel);
                }

                return OilCanItem.setFuel(itemstack.copy(), startFuel + fuel);
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
            int configType = json.get("config_type").getAsInt();

            return new OilCanRecipe(recipe.getId(), recipe.getGroup(), recipe.getOutput(), recipe.getIngredients(), fuel, configType);
        }

        @Override
        public OilCanRecipe read(Identifier resourceLocation, PacketByteBuf friendlyByteBuf) {
            ShapelessRecipe recipe = ShapelessRecipe.Serializer.SHAPELESS.read(resourceLocation, friendlyByteBuf);
            int fuelValue = friendlyByteBuf.readVarInt();
            int configType = friendlyByteBuf.readVarInt();

            return new OilCanRecipe(recipe.getId(), recipe.getGroup(), recipe.getOutput(), recipe.getIngredients(), fuelValue, configType);
        }

        @Override
        public void write(PacketByteBuf friendlyByteBuf, OilCanRecipe oilCanRecipe) {
            ShapelessRecipe rec = new ShapelessRecipe(oilCanRecipe.getId(), oilCanRecipe.getGroup(), oilCanRecipe.getOutput(), oilCanRecipe.getIngredients());
            ShapelessRecipe.Serializer.SHAPELESS.write(friendlyByteBuf, rec);

            friendlyByteBuf.writeVarInt(oilCanRecipe.fuelAmount);
            friendlyByteBuf.writeVarInt(oilCanRecipe.configType);
        }
    }
}
