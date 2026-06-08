package com.galgochim.client;

import com.galgochim.Galgochim;
import net.minecraft.client.model.geom.ModelLayerLocation;

public final class ModModelLayers {
    private ModModelLayers() {
    }

    public static final ModelLayerLocation GALGOACH = create("galgoach");
    public static final ModelLayerLocation PILAPA = create("pilapa");
    public static final ModelLayerLocation KARNER = create("karner");

    private static ModelLayerLocation create(String name) {
        return new ModelLayerLocation(Galgochim.id(name), "main");
    }
}
