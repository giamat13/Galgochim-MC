package com.galgochim.client.renderer;

import com.galgochim.Galgochim;
import com.galgochim.client.ModModelLayers;
import com.galgochim.client.model.ShipModel;
import com.galgochim.entity.AlienShipEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class AlienShipRenderer extends SimpleMobRenderer<AlienShipEntity, ShipModel> {
    private static final Identifier TEXTURE = Galgochim.id("textures/entity/alien_ship.png");

    public AlienShipRenderer(EntityRendererProvider.Context context) {
        super(context, new ShipModel(context.bakeLayer(ModModelLayers.ALIEN_SHIP)), 0.0f, TEXTURE);
    }
}
