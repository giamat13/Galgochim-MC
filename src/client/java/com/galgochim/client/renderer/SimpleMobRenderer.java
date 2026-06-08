package com.galgochim.client.renderer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Mob;

/**
 * Shared renderer for the mod's mobs: they all use a plain
 * {@link LivingEntityRenderState} and a single fixed texture.
 */
public abstract class SimpleMobRenderer<T extends Mob, M extends EntityModel<LivingEntityRenderState>>
        extends MobRenderer<T, LivingEntityRenderState, M> {

    private final Identifier texture;

    protected SimpleMobRenderer(EntityRendererProvider.Context context, M model, float shadowRadius, Identifier texture) {
        super(context, model, shadowRadius);
        this.texture = texture;
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return this.texture;
    }
}
