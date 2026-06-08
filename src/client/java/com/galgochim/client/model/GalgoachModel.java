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
 * A chicken-like bird that rolls on two wheels instead of legs.
 * Texture: 64x64.
 */
public class GalgoachModel extends EntityModel<LivingEntityRenderState> {
    private final ModelPart head;
    private final ModelPart wheelLeft;
    private final ModelPart wheelRight;

    public GalgoachModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.wheelLeft = root.getChild("wheel_left");
        this.wheelRight = root.getChild("wheel_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Body bottom sits at y=18, directly on top of the wheels.
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-5.0f, -10.0f, -7.0f, 10.0f, 10.0f, 13.0f),
                PartPose.offset(0.0f, 18.0f, 0.0f));

        // Head bottom (y=2..8) meets the body top at y=8.
        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 22).addBox(-3.0f, -6.0f, -3.0f, 6.0f, 6.0f, 5.0f),
                PartPose.offset(0.0f, 8.0f, -6.0f));
        // beak
        head.addOrReplaceChild("beak",
                CubeListBuilder.create().texOffs(24, 0).addBox(-1.5f, -3.0f, -6.0f, 3.0f, 3.0f, 3.0f),
                PartPose.ZERO);

        // Two wheels where the legs would be: they tuck up against the body
        // underside (top at y=18) and roll on the ground (bottom at y=24).
        root.addOrReplaceChild("wheel_left",
                CubeListBuilder.create().texOffs(0, 33).addBox(-1.0f, -3.0f, -3.0f, 1.0f, 6.0f, 6.0f),
                PartPose.offset(4.0f, 21.0f, 0.0f));
        root.addOrReplaceChild("wheel_right",
                CubeListBuilder.create().texOffs(16, 33).addBox(0.0f, -3.0f, -3.0f, 1.0f, 6.0f, 6.0f),
                PartPose.offset(-4.0f, 21.0f, 0.0f));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        this.head.xRot = state.xRot * ((float) Math.PI / 180f);
        this.head.yRot = state.yRot * ((float) Math.PI / 180f);
        // The wheels spin as the Galgoach rolls along.
        float spin = state.walkAnimationPos * 0.9f;
        this.wheelLeft.xRot = spin;
        this.wheelRight.xRot = spin;
    }
}
