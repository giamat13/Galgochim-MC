package com.galgochim.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

/**
 * A simple flying-saucer model shared by the Hagit and the alien ship:
 * a wide flat hull with a small dome on top. Texture: 128x128.
 */
public class ShipModel extends EntityModel<LivingEntityRenderState> {

    public ShipModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("hull",
                CubeListBuilder.create().texOffs(0, 0).addBox(-16.0f, -2.0f, -16.0f, 32.0f, 4.0f, 32.0f),
                PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("dome",
                CubeListBuilder.create().texOffs(0, 40).addBox(-6.0f, -8.0f, -6.0f, 12.0f, 6.0f, 12.0f),
                PartPose.offset(0.0f, 0.0f, 0.0f));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
    }
}
