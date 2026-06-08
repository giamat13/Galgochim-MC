package com.galgochim.client.renderer;

import com.galgochim.Galgochim;
import com.galgochim.client.ModModelLayers;
import com.galgochim.client.model.KarnerModel;
import com.galgochim.entity.KarnerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class KarnerRenderer extends SimpleMobRenderer<KarnerEntity, KarnerModel> {
    private static final Identifier TEXTURE = Galgochim.id("textures/entity/karner.png");

    public KarnerRenderer(EntityRendererProvider.Context context) {
        super(context, new KarnerModel(context.bakeLayer(ModModelLayers.KARNER)), 0.6f, TEXTURE);
    }
}
