package com.github.wolfiewaffle.hardcore_torches;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.impl.client.rendering.BlockEntityRendererRegistryImpl;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.CampfireBlockEntityRenderer;

public class ClientMod implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This makes it so the torches don't render with black instead of transparency
		BlockRenderLayerMap.INSTANCE.putBlock(Mod.LIT_TORCH, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Mod.UNLIT_TORCH, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Mod.SMOLDERING_TORCH, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Mod.BURNT_TORCH, RenderLayer.getCutout());

		BlockRenderLayerMap.INSTANCE.putBlock(Mod.LIT_WALL_TORCH, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Mod.UNLIT_WALL_TORCH, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Mod.SMOLDERING_WALL_TORCH, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Mod.BURNT_WALL_TORCH, RenderLayer.getCutout());

		BlockRenderLayerMap.INSTANCE.putBlock(Mod.LIT_LANTERN, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Mod.UNLIT_LANTERN, RenderLayer.getCutout());

		//BlockRenderLayerMap.INSTANCE.putBlock(Mod.CAMPFIRE_BLOCK, RenderLayer.getCutout());

		//BlockRenderLayerMap.INSTANCE.putBlock(Mod.CAMPFIRE_BLOCK, RenderLayer.getCutout());
		//BlockEntityRendererFactories.register(Mod.CAMPFIRE_BLOCK_ENTITY, CampfireBlockEntityRenderer::new);
	}
}
