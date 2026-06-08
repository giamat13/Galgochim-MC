package com.galgochim.client.renderer;

import com.galgochim.Galgochim;
import com.galgochim.client.ModModelLayers;
import com.galgochim.client.model.PilapaModel;
import com.galgochim.entity.PilapaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class PilapaRenderer extends SimpleMobRenderer<PilapaEntity, PilapaModel> {
    private static final Identifier TEXTURE = Galgochim.id("textures/entity/pilapa.png");

    public PilapaRenderer(EntityRendererProvider.Context context) {
        super(context, new PilapaModel(context.bakeLayer(ModModelLayers.PILAPA)), 0.6f, TEXTURE);
    }
}
