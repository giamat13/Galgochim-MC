package com.galgochim.client.renderer;

import com.galgochim.Galgochim;
import com.galgochim.client.ModModelLayers;
import com.galgochim.client.model.ShipModel;
import com.galgochim.entity.HagitEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class HagitRenderer extends SimpleMobRenderer<HagitEntity, ShipModel> {
    private static final Identifier TEXTURE = Galgochim.id("textures/entity/hagit.png");

    public HagitRenderer(EntityRendererProvider.Context context) {
        super(context, new ShipModel(context.bakeLayer(ModModelLayers.HAGIT)), 0.0f, TEXTURE);
    }
}
