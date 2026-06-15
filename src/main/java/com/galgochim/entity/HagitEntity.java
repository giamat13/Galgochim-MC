package com.galgochim.entity;

import com.galgochim.registry.ModItems;
import com.galgochim.registry.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * The Hagit (חגית) spaceship. It drifts across the night sky and continues on
 * its way. Shoot it with an arrow and it drops a chrono-thing. While it is
 * around (and close) you can hear "The captain said".
 */
public class HagitEntity extends Mob {

    private Vec3 heading = new Vec3(0.4, 0.0, 0.0);
    private int lifetime = 2400; // ~2 minutes, then it leaves
    private boolean droppedChrono;
    /** True while a player is close enough to have heard the captain (avoids replaying). */
    private boolean captainAnnounced;
    /** Players only hear the captain when the ship is this close (blocks). */
    private static final double CAPTAIN_RANGE = 20.0;

    public HagitEntity(EntityType<? extends HagitEntity> type, Level level) {
        super(type, level);
        this.setNoAi(true);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0);
    }

    @Override
    protected void registerGoals() {
        // No AI - it drifts in a straight line (see tick).
    }

    /** Aim the ship along a horizontal heading at the given speed. */
    public void setHeading(Vec3 dir) {
        this.heading = dir;
    }

    @Override
    public void tick() {
        super.tick();
        // Drift across the sky, ignoring blocks (noPhysics).
        this.setPos(this.getX() + this.heading.x, this.getY() + this.heading.y, this.getZ() + this.heading.z);

        if (this.level() instanceof ServerLevel server) {
            // Only "the captain speaks" when a player is really close, and only
            // once per fly-by (re-armed once everyone is out of range) so the clip
            // never stacks on top of itself.
            boolean playerClose = server.getNearestPlayer(this, CAPTAIN_RANGE) != null;
            if (playerClose && !this.captainAnnounced) {
                server.playSound(null, this.getX(), this.getY(), this.getZ(),
                        ModSounds.AMAR_HAKAPTAN, SoundSource.HOSTILE, 1.0f, 1.0f);
                this.captainAnnounced = true;
            } else if (!playerClose) {
                this.captainAnnounced = false;
            }
            if (--this.lifetime <= 0) {
                this.discard();
            }
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (!this.droppedChrono
                && (source.getDirectEntity() instanceof Projectile || source.getEntity() instanceof Projectile)) {
            this.spawnAtLocation(level, ModItems.CHRONO);
            this.droppedChrono = true;
        }
        return super.hurtServer(level, source, amount);
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }
}
