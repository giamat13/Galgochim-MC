package com.galgochim.registry;

import com.galgochim.Galgochim;
import com.galgochim.entity.AlienShipEntity;
import com.galgochim.entity.GalgoachEntity;
import com.galgochim.entity.HagitEntity;
import com.galgochim.entity.KarnerEntity;
import com.galgochim.entity.PilapaEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ModEntities {
    private ModEntities() {
    }

    public static final EntityType<GalgoachEntity> GALGOACH = register(
            "galgoach",
            EntityType.Builder.of(GalgoachEntity::new, MobCategory.CREATURE).sized(0.9f, 1.3f));

    public static final EntityType<PilapaEntity> PILAPA = register(
            "pilapa",
            EntityType.Builder.of(PilapaEntity::new, MobCategory.CREATURE).sized(0.9f, 2.1f));

    public static final EntityType<KarnerEntity> KARNER = register(
            "karner",
            EntityType.Builder.of(KarnerEntity::new, MobCategory.MONSTER).sized(0.8f, 1.6f));

    // Spaceships: large, never naturally spawned (summoned/scripted), and never despawn-culled.
    public static final EntityType<HagitEntity> HAGIT = register(
            "hagit",
            EntityType.Builder.of(HagitEntity::new, MobCategory.MISC).sized(4.0f, 1.5f).fireImmune());

    public static final EntityType<AlienShipEntity> ALIEN_SHIP = register(
            "alien_ship",
            EntityType.Builder.of(AlienShipEntity::new, MobCategory.MISC).sized(4.0f, 1.5f).fireImmune());

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Galgochim.id(name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    /** Triggers static init (registration) and wires up default attributes. */
    public static void register() {
        FabricDefaultAttributeRegistry.register(GALGOACH, GalgoachEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(PILAPA, PilapaEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(KARNER, KarnerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(HAGIT, HagitEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ALIEN_SHIP, AlienShipEntity.createAttributes());
    }
}
