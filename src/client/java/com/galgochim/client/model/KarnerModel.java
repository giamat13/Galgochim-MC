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
 * A four-legged predator with a horned head that hunts Pilapot.
 * Texture: 64x64.
 */
public class KarnerModel extends EntityModel<LivingEntityRenderState> {
    private final ModelPart head;
    private final ModelPart legFrontLeft;
    private final ModelPart legFrontRight;
    private final ModelPart legBackLeft;
    private final ModelPart legBackRight;

    public KarnerModel(ModelPart root) {
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

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -4.0f, -7.0f, 8.0f, 8.0f, 14.0f),
                PartPose.offset(0.0f, 12.0f, 0.0f));

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 22).addBox(-3.5f, -3.5f, -6.0f, 7.0f, 7.0f, 6.0f)
                        .texOffs(28, 0).addBox(-1.5f, 1.0f, -9.0f, 3.0f, 2.0f, 3.0f), // snout
                PartPose.offset(0.0f, 10.0f, -7.0f));
        // Two horns.
        head.addOrReplaceChild("horn_left",
                CubeListBuilder.create().texOffs(0, 35).addBox(0.0f, -6.0f, -2.0f, 1.0f, 4.0f, 1.0f),
                PartPose.offset(2.5f, -3.5f, 0.0f));
        head.addOrReplaceChild("horn_right",
                CubeListBuilder.create().texOffs(8, 35).addBox(-1.0f, -6.0f, -2.0f, 1.0f, 4.0f, 1.0f),
                PartPose.offset(-2.5f, -3.5f, 0.0f));

        CubeListBuilder leg = CubeListBuilder.create().texOffs(40, 0).addBox(-1.5f, 0.0f, -1.5f, 3.0f, 8.0f, 3.0f);
        root.addOrReplaceChild("leg_front_left", leg, PartPose.offset(2.5f, 16.0f, -4.5f));
        root.addOrReplaceChild("leg_front_right", leg, PartPose.offset(-2.5f, 16.0f, -4.5f));
        root.addOrReplaceChild("leg_back_left", leg, PartPose.offset(2.5f, 16.0f, 4.5f));
        root.addOrReplaceChild("leg_back_right", leg, PartPose.offset(-2.5f, 16.0f, 4.5f));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        this.head.xRot = state.xRot * ((float) Math.PI / 180f);
        this.head.yRot = state.yRot * ((float) Math.PI / 180f);

        float swing = (float) Math.cos(state.walkAnimationPos * 0.6662f) * 1.4f * state.walkAnimationSpeed;
        this.legFrontLeft.xRot = -swing;
        this.legFrontRight.xRot = swing;
        this.legBackLeft.xRot = swing;
        this.legBackRight.xRot = -swing;
    }
}
