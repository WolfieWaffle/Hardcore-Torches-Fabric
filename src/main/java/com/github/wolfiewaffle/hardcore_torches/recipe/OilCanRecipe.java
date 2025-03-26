//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.recipe;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.item.OilCanItem;
import com.google.gson.JsonObject;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
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

  public boolean matches(RecipeInputInventory craftingInventory, World world) {
    if (this.configType == 0 && !Mod.config.enableFatOil) {
      return false;
    } else {
      return this.configType == 1 && !Mod.config.enableCoalOil ? false : super.matches(craftingInventory, world);
    }
  }

  public ItemStack craft(RecipeInputInventory grid, DynamicRegistryManager dynamicRegistryManager) {
    for(int i = 0; i < grid.size(); ++i) {
      ItemStack itemstack = grid.getStack(i);
      if (itemstack.getItem() instanceof OilCanItem) {
        OilCanItem can = (OilCanItem)itemstack.getItem();
        int startFuel = OilCanItem.getFuel(itemstack);
        int fuel = (int)((float)this.fuelAmount * Mod.config.oilRecipeMultiplier);
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
    private static final Identifier NAME = new Identifier("hardcore_torches", "oil_can");

    public Serializer() {
    }

    public OilCanRecipe read(Identifier resourceLocation, JsonObject json) {
      ShapelessRecipe recipe = (ShapelessRecipe)net.minecraft.recipe.ShapelessRecipe.Serializer.SHAPELESS.read(resourceLocation, json);
      int fuel = json.get("fuel").getAsInt();
      int configType = json.get("config_type").getAsInt();
      ItemStack output = ItemStack.EMPTY;

      try {
        output = recipe.getOutput((DynamicRegistryManager)null);
      } catch (NullPointerException var8) {
        System.out.println("Hardcore Torches, NULL OUTPUT OR SOMETHING");
      }

      return new OilCanRecipe(recipe.getId(), recipe.getGroup(), output, recipe.getIngredients(), fuel, configType);
    }

    public OilCanRecipe read(Identifier resourceLocation, PacketByteBuf friendlyByteBuf) {
      ShapelessRecipe recipe = (ShapelessRecipe)net.minecraft.recipe.ShapelessRecipe.Serializer.SHAPELESS.read(resourceLocation, friendlyByteBuf);
      int fuelValue = friendlyByteBuf.readVarInt();
      int configType = friendlyByteBuf.readVarInt();
      ItemStack output = ItemStack.EMPTY;

      try {
        output = recipe.getOutput((DynamicRegistryManager)null);
      } catch (NullPointerException var8) {
        System.out.println("Hardcore Torches, NULL OUTPUT OR SOMETHING");
      }

      return new OilCanRecipe(recipe.getId(), recipe.getGroup(), output, recipe.getIngredients(), fuelValue, configType);
    }

    public void write(PacketByteBuf friendlyByteBuf, OilCanRecipe oilCanRecipe) {
      ItemStack output = ItemStack.EMPTY;

      try {
        output = oilCanRecipe.getOutput((DynamicRegistryManager)null);
      } catch (NullPointerException var5) {
        System.out.println("Hardcore Torches, NULL OUTPUT OR SOMETHING");
      }

      ShapelessRecipe rec = new ShapelessRecipe(oilCanRecipe.getId(), oilCanRecipe.getGroup(), CraftingRecipeCategory.EQUIPMENT, output, oilCanRecipe.getIngredients());
      net.minecraft.recipe.ShapelessRecipe.Serializer.SHAPELESS.write(friendlyByteBuf, rec);
      friendlyByteBuf.writeVarInt(oilCanRecipe.fuelAmount);
      friendlyByteBuf.writeVarInt(oilCanRecipe.configType);
    }
  }
}
