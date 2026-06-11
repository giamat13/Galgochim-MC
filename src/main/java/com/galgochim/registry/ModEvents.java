package com.galgochim.registry;

import java.util.List;

import com.galgochim.entity.AlienShipEntity;
import com.galgochim.entity.HagitEntity;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.phys.Vec3;

/**
 * Server-side glue for issue #1: the nightly fly-overs of the Hagit and the
 * alien ship, plus the 1% Wilhelm scream on any hit.
 */
public final class ModEvents {
    private ModEvents() {
    }

    /** Whether the night fly-overs are currently enabled (toggled by commands). */
    public static volatile boolean hagitEnabled = false;
    public static volatile boolean aliensEnabled = false;

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(ModEvents::onWorldTick);

        // 1% chance on every hit to anything: play the Wilhelm scream.
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) -> {
            if (entity.level() instanceof ServerLevel server && server.random.nextFloat() < 0.01f) {
                server.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        ModSounds.WILHELM_SCREAM, SoundSource.PLAYERS, 1.0f, 1.0f);
            }
        });
    }

    private static void onWorldTick(ServerLevel level) {
        if (!level.dimensionType().natural() || !isNight(level)) {
            return;
        }
        // Rare nightly chance to send a ship drifting over a random player.
        List<ServerPlayer> players = level.players();
        if (players.isEmpty()) {
            return;
        }
        if (hagitEnabled && level.random.nextInt(2400) == 0) {
            summonHagit(level, players.get(level.random.nextInt(players.size())));
        }
        if (aliensEnabled && level.random.nextInt(2400) == 0) {
            summonAlien(level, players.get(level.random.nextInt(players.size())));
        }
    }

    /** Spawn a Hagit that drifts across the sky over the given player. */
    public static HagitEntity summonHagit(ServerLevel level, ServerPlayer player) {
        HagitEntity ship = ModEntities.HAGIT.create(level, EntitySpawnReason.COMMAND);
        if (ship != null) {
            placeOverhead(level, ship, player);
            ship.setHeading(headingTowards(player, ship));
            level.addFreshEntity(ship);
        }
        return ship;
    }

    /** Spawn an alien ship that drifts across the sky over the given player. */
    public static AlienShipEntity summonAlien(ServerLevel level, ServerPlayer player) {
        AlienShipEntity ship = ModEntities.ALIEN_SHIP.create(level, EntitySpawnReason.COMMAND);
        if (ship != null) {
            placeOverhead(level, ship, player);
            ship.setHeading(headingTowards(player, ship));
            level.addFreshEntity(ship);
        }
        return ship;
    }

    private static void placeOverhead(ServerLevel level, Entity ship, ServerPlayer player) {
        double angle = level.random.nextDouble() * Math.PI * 2.0;
        double radius = 40.0;
        double x = player.getX() - Math.cos(angle) * radius;
        double z = player.getZ() - Math.sin(angle) * radius;
        double y = player.getY() + 30.0;
        ship.moveTo(x, y, z, 0.0f, 0.0f);
    }

    /** Vanilla "night" window, computed from the day time to avoid version-specific helpers. */
    private static boolean isNight(ServerLevel level) {
        long t = level.getDayTime() % 24000L;
        return t >= 13000L && t <= 23000L;
    }

    private static Vec3 headingTowards(ServerPlayer player, Entity ship) {
        Vec3 dir = new Vec3(player.getX() - ship.getX(), 0.0, player.getZ() - ship.getZ());
        if (dir.lengthSqr() < 1.0e-4) {
            return new Vec3(0.4, 0.0, 0.0);
        }
        return dir.normalize().scale(0.4);
    }
}
