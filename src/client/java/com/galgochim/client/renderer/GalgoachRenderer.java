package com.galgochim.client.renderer;

import com.galgochim.Galgochim;
import com.galgochim.client.ModModelLayers;
import com.galgochim.client.model.GalgoachModel;
import com.galgochim.entity.GalgoachEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class GalgoachRenderer extends SimpleMobRenderer<GalgoachEntity, GalgoachModel> {
    private static final Identifier TEXTURE = Galgochim.id("textures/entity/galgoach.png");

    public GalgoachRenderer(EntityRendererProvider.Context context) {
        super(context, new GalgoachModel(context.bakeLayer(ModModelLayers.GALGOACH)), 0.5f, TEXTURE);
    }
}
