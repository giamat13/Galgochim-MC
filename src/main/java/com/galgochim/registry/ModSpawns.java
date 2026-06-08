package com.galgochim.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;

/** Natural world spawning: where the mod's mobs appear and on what ground. */
public final class ModSpawns {
    private ModSpawns() {
    }

    public static void register() {
        // Spawn placement rules (valid ground, light, etc.).
        SpawnPlacements.register(ModEntities.GALGOACH, SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        SpawnPlacements.register(ModEntities.PILAPA, SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        // The Karner is a predator that roams day and night.
        SpawnPlacements.register(ModEntities.KARNER, SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules);

        // High spawn weights (well above vanilla cows/sheep) so the mod's mobs
        // are a very common sight in their biomes.

        // Galgochim live in forests and plains.
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(BiomeTags.IS_FOREST)
                        .or(BiomeSelectors.includeByKey(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS)),
                MobCategory.CREATURE, ModEntities.GALGOACH, 24, 4, 6);

        // Pilapot graze the savanna, where Karners hunt them.
        BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_SAVANNA),
                MobCategory.CREATURE, ModEntities.PILAPA, 24, 4, 6);
        BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_SAVANNA),
                MobCategory.MONSTER, ModEntities.KARNER, 16, 2, 4);
    }
}
