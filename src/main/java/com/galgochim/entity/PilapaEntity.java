package com.galgochim.entity;

import com.galgochim.Galgochim;
import com.galgochim.registry.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * The Pilapa (פילאפה): a tall animal with long legs. Peaceful, but it sprints
 * away from any Karner that comes hunting it.
 */
public class PilapaEntity extends Animal {

    /**
     * Issue #4: breeding two Pilapot can yield an improved "fancy" Pilapa that
     * runs faster and stands taller (longer legs). These permanent attribute
     * modifiers are saved/restored by the vanilla attribute system, so the
     * variant survives reloads without any custom NBT.
     */
    private static final AttributeModifier FANCY_SPEED = new AttributeModifier(
            Galgochim.id("fancy_pilapa_speed"), 0.4, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    private static final AttributeModifier FANCY_SCALE = new AttributeModifier(
            Galgochim.id("fancy_pilapa_scale"), 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    /** Chance that a bred Pilapa is born as the improved variant. */
    private static final float FANCY_CHANCE = 0.2f;

    public PilapaEntity(EntityType<? extends PilapaEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 14.0)
                // Long legs: it is a fast runner.
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.8));
        // Flee from Karners that want to eat it.
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, KarnerEntity.class, 12.0f, 1.8, 2.0));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.1, stack -> stack.is(Items.WHEAT), false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.WHEAT);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        PilapaEntity child = ModEntities.PILAPA.create(level, EntitySpawnReason.BREEDING);
        if (child != null && this.random.nextFloat() < FANCY_CHANCE) {
            child.makeFancy();
        }
        return child;
    }

    /** Turn this Pilapa into the faster, taller "fancy" variant. */
    public void makeFancy() {
        AttributeInstance speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null && !speed.hasModifier(FANCY_SPEED.id())) {
            speed.addPermanentModifier(FANCY_SPEED);
        }
        // SCALE is a default attribute on every living entity; if it is somehow
        // absent we simply skip the height boost rather than crashing.
        AttributeInstance scale = this.getAttribute(Attributes.SCALE);
        if (scale != null && !scale.hasModifier(FANCY_SCALE.id())) {
            scale.addPermanentModifier(FANCY_SCALE);
        }
    }
}
