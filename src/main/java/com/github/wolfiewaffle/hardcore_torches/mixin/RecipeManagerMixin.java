package com.github.wolfiewaffle.hardcore_torches.mixin;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import com.google.gson.JsonElement;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Inject(method = "apply", at = @At("HEAD"))
    public void interceptApply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        if (Mod.TORCH_RECIPE != null) {
            map.put(new Identifier("minecraft", "torch"), Mod.TORCH_RECIPE);
        }

        if (Mod.VANILLA_TORCH_RECIPE != null) {
            map.put(new Identifier("hardcore_torches", "vanilla_torch"), Mod.VANILLA_TORCH_RECIPE);
        }

        if (Mod.LANTERN_RECIPE != null) {
            map.put(new Identifier("hardcore_torches", "lantern"), Mod.LANTERN_RECIPE);
        }
    }

}