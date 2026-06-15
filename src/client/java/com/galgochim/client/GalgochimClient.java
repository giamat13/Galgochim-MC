package com.galgochim.client;

import com.galgochim.client.model.GalgoachModel;
import com.galgochim.client.model.KarnerModel;
import com.galgochim.client.model.PilapaModel;
import com.galgochim.client.model.ShipModel;
import com.galgochim.client.renderer.AlienShipRenderer;
import com.galgochim.client.renderer.GalgoachRenderer;
import com.galgochim.client.renderer.HagitRenderer;
import com.galgochim.client.renderer.KarnerRenderer;
import com.galgochim.client.renderer.PilapaRenderer;
import com.galgochim.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;

public class GalgochimClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModelLayerRegistry.registerModelLayer(ModModelLayers.GALGOACH, GalgoachModel::createBodyLayer);
		ModelLayerRegistry.registerModelLayer(ModModelLayers.PILAPA, PilapaModel::createBodyLayer);
		ModelLayerRegistry.registerModelLayer(ModModelLayers.KARNER, KarnerModel::createBodyLayer);
		ModelLayerRegistry.registerModelLayer(ModModelLayers.HAGIT, ShipModel::createBodyLayer);
		ModelLayerRegistry.registerModelLayer(ModModelLayers.ALIEN_SHIP, ShipModel::createBodyLayer);

		EntityRendererRegistry.register(ModEntities.GALGOACH, GalgoachRenderer::new);
		EntityRendererRegistry.register(ModEntities.PILAPA, PilapaRenderer::new);
		EntityRendererRegistry.register(ModEntities.KARNER, KarnerRenderer::new);
		EntityRendererRegistry.register(ModEntities.HAGIT, HagitRenderer::new);
		EntityRendererRegistry.register(ModEntities.ALIEN_SHIP, AlienShipRenderer::new);
	}
}
