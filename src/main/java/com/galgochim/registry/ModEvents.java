package com.galgochim.registry;

import java.util.List;

import com.galgochim.block.WashingMachineBlock;
import com.galgochim.entity.AlienShipEntity;
import com.galgochim.entity.HagitEntity;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.state.BlockState;
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
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerLevel level : server.getAllLevels()) {
                onWorldTick(level);
            }
        });

        // 1% chance on every hit to anything: play the Wilhelm scream.
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) -> {
            if (entity.level() instanceof ServerLevel server && server.getRandom().nextFloat() < 0.01f) {
                server.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        ModSounds.WILHELM_SCREAM, SoundSource.PLAYERS, 1.0f, 1.0f);
            }
        });

        // Make Doron Fishler turn up naturally: about 1 in 20 unemployed adult
        // villagers becomes a Doron. The decision is derived from the villager's
        // UUID so it is stable across reloads (it can't snowball over time), and
        // we bump trade XP so the profession sticks without a microphone nearby.
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof Villager villager
                    && !villager.isBaby()
                    && villager.getVillagerData().profession().is(VillagerProfession.NONE)
                    && Math.floorMod(villager.getUUID().hashCode(), 20) == 0) {
                villager.setVillagerData(
                        villager.getVillagerData().withProfession(world.registryAccess(), ModVillagers.DORON_KEY));
                villager.setVillagerXp(Math.max(1, villager.getVillagerXp()));
            }
        });
    }

    private static void onWorldTick(ServerLevel level) {
        List<ServerPlayer> players = level.players();
        if (players.isEmpty()) {
            return;
        }

        // Boats near a jukebox playing "Tekef Yavo Shekemist" go twice as fast (any time of day).
        if (level.getGameTime() % 4L == 0L) {
            boostBoatsNearShekemist(level, players);
        }

        // Vanilla "night" window, derived from the game time (getGameTime is the
        // one time accessor available in these mappings).
        long timeOfDay = level.getGameTime() % 24000L;
        if (timeOfDay < 13000L || timeOfDay > 23000L) {
            return;
        }
        if (hagitEnabled && level.getRandom().nextInt(2400) == 0) {
            summonHagit(level, players.get(level.getRandom().nextInt(players.size())));
        }
        if (aliensEnabled && level.getRandom().nextInt(2400) == 0) {
            summonAlien(level, players.get(level.getRandom().nextInt(players.size())));
        }
        // Bait: a full washing machine under open sky lures aliens even without the command.
        if (level.getGameTime() % 100L == 0L) {
            ServerPlayer player = players.get(level.getRandom().nextInt(players.size()));
            if (hasBaitNear(level, player.blockPosition()) && level.getRandom().nextInt(20) == 0) {
                summonAlien(level, player);
            }
        }
    }

    /** Speed up boats near a jukebox that is playing the Shekemist disc. */
    private static void boostBoatsNearShekemist(ServerLevel level, List<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            for (AbstractBoat boat : level.getEntitiesOfClass(
                    AbstractBoat.class, player.getBoundingBox().inflate(24.0, 8.0, 24.0))) {
                if (hasShekemistJukeboxNear(level, boat.blockPosition())) {
                    Vec3 v = boat.getDeltaMovement();
                    double speed = Math.sqrt(v.x * v.x + v.z * v.z);
                    if (speed > 0.05) {
                        // Scale the horizontal velocity to 2x, capped so it can't run away.
                        double factor = Math.min(speed * 2.0, 0.8) / speed;
                        boat.setDeltaMovement(v.x * factor, v.y, v.z * factor);
                        boat.hurtMarked = true; // push the new velocity to the client
                    }
                }
            }
        }
    }

    private static boolean hasShekemistJukeboxNear(ServerLevel level, BlockPos center) {
        int r = 6;
        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-r, -r, -r), center.offset(r, r, r))) {
            BlockState state = level.getBlockState(pos);
            if (state.is(Blocks.JUKEBOX) && state.getValue(JukeboxBlock.HAS_RECORD)
                    && level.getBlockEntity(pos) instanceof Container container
                    && container.getItem(0).is(ModItems.MUSIC_DISC_TEKEF_YAVO_SHEKEMIST)) {
                return true;
            }
        }
        return false;
    }

    /** True if a full washing machine stands under open sky near the position. */
    private static boolean hasBaitNear(ServerLevel level, BlockPos center) {
        int r = 8;
        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-r, -r, -r), center.offset(r, r, r))) {
            BlockState state = level.getBlockState(pos);
            if (state.is(ModBlocks.WASHING_MACHINE) && state.getValue(WashingMachineBlock.FILLED)
                    && level.canSeeSky(pos.above())) {
                return true;
            }
        }
        return false;
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
        double angle = level.getRandom().nextDouble() * Math.PI * 2.0;
        double radius = 40.0;
        double x = player.getX() - Math.cos(angle) * radius;
        double z = player.getZ() - Math.sin(angle) * radius;
        double y = player.getY() + 30.0;
        ship.setPos(x, y, z);
    }

    private static Vec3 headingTowards(ServerPlayer player, Entity ship) {
        Vec3 dir = new Vec3(player.getX() - ship.getX(), 0.0, player.getZ() - ship.getZ());
        if (dir.lengthSqr() < 1.0e-4) {
            return new Vec3(0.4, 0.0, 0.0);
        }
        return dir.normalize().scale(0.4);
    }
}
