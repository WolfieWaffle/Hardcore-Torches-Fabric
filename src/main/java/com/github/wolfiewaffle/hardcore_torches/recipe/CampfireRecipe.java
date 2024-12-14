package com.github.wolfiewaffle.hardcore_torches.recipe;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.github.wolfiewaffle.hardcore_torches.item.TorchItem;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.dynamic.Codecs;

public class CampfireRecipe extends ShapedRecipe {
    private final RawShapedRecipe raw;
    private final ItemStack result;

    public CampfireRecipe(String group, RawShapedRecipe raw, ItemStack result) {
        super(group, CraftingRecipeCategory.EQUIPMENT, raw, result, false);
        this.raw = raw;
        this.result = result;
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

        public static final Codec<CampfireRecipe> CODEC = RecordCodecBuilder.create((instance) -> instance.group(

                Codecs.createStrictOptionalFieldCodec(Codec.STRING, "group", "").forGetter((recipe) -> recipe.getGroup()),

                RawShapedRecipe.CODEC.forGetter((recipe) -> recipe.raw),

                ItemStack.RECIPE_RESULT_CODEC.fieldOf("result").forGetter((recipe) -> recipe.result)

        ).apply(instance, CampfireRecipe::new));

        public Serializer() {
        }

        @Override
        public Codec<CampfireRecipe> codec() {
            return CODEC;
        }

        @Override
        public CampfireRecipe read(PacketByteBuf buf) {
            return null;
        }

        @Override
        public void write(PacketByteBuf buf, CampfireRecipe recipe) {

        }
    }
}
