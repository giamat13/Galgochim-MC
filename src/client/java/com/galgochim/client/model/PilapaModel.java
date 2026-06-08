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
 * A tall animal standing on long legs, with a long neck.
 * Texture: 64x64.
 */
public class PilapaModel extends EntityModel<LivingEntityRenderState> {
    private final ModelPart head;
    private final ModelPart legFrontLeft;
    private final ModelPart legFrontRight;
    private final ModelPart legBackLeft;
    private final ModelPart legBackRight;

    public PilapaModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.legFrontLeft = root.getChild("leg_front_left");
        this.legFrontRight = root.getChild("leg_front_right");
        this.legBackLeft = root.getChild("leg_back_left");
        this.legBackRight = root.getChild("leg_back_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Compact body whose bottom sits at y=11, where the legs attach.
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, 0.0f, -6.0f, 8.0f, 8.0f, 12.0f),
                PartPose.offset(0.0f, 3.0f, 0.0f));

        // Long neck + head, rising from the body top at y=3.
        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 20).addBox(-1.5f, -10.0f, -1.5f, 3.0f, 10.0f, 3.0f) // neck
                        .texOffs(14, 20).addBox(-2.0f, -13.0f, -4.0f, 4.0f, 4.0f, 4.0f), // head
                PartPose.offset(0.0f, 3.0f, -5.0f));

        // Four long legs: tops attach to the body underside (y=11), feet at y=24.
        CubeListBuilder leg = CubeListBuilder.create().texOffs(28, 0).addBox(-1.0f, 0.0f, -1.0f, 2.0f, 13.0f, 2.0f);
        root.addOrReplaceChild("leg_front_left", leg, PartPose.offset(3.0f, 11.0f, -4.0f));
        root.addOrReplaceChild("leg_front_right", leg, PartPose.offset(-3.0f, 11.0f, -4.0f));
        root.addOrReplaceChild("leg_back_left", leg, PartPose.offset(3.0f, 11.0f, 4.0f));
        root.addOrReplaceChild("leg_back_right", leg, PartPose.offset(-3.0f, 11.0f, 4.0f));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        this.head.xRot = state.xRot * ((float) Math.PI / 180f);
        this.head.yRot = state.yRot * ((float) Math.PI / 180f);

        float swing = (float) Math.cos(state.walkAnimationPos * 0.6662f) * 1.0f * state.walkAnimationSpeed;
        this.legFrontLeft.xRot = -swing;
        this.legFrontRight.xRot = swing;
        this.legBackLeft.xRot = swing;
        this.legBackRight.xRot = -swing;
    }
}
