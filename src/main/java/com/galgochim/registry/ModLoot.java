package com.galgochim.registry;

import java.util.Set;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

/** Adds the mod's records to vanilla chest loot. */
public final class ModLoot {
    private ModLoot() {
    }

    /** Every village chest where the Fishland Anthem might turn up. */
    private static final Set<ResourceKey<LootTable>> VILLAGE_TABLES = Set.of(
            BuiltInLootTables.VILLAGE_WEAPONSMITH,
            BuiltInLootTables.VILLAGE_TOOLSMITH,
            BuiltInLootTables.VILLAGE_ARMORER,
            BuiltInLootTables.VILLAGE_CARTOGRAPHER,
            BuiltInLootTables.VILLAGE_MASON,
            BuiltInLootTables.VILLAGE_SHEPHERD,
            BuiltInLootTables.VILLAGE_BUTCHER,
            BuiltInLootTables.VILLAGE_FLETCHER,
            BuiltInLootTables.VILLAGE_FISHER,
            BuiltInLootTables.VILLAGE_TANNERY,
            BuiltInLootTables.VILLAGE_TEMPLE,
            BuiltInLootTables.VILLAGE_DESERT_HOUSE,
            BuiltInLootTables.VILLAGE_PLAINS_HOUSE,
            BuiltInLootTables.VILLAGE_TAIGA_HOUSE,
            BuiltInLootTables.VILLAGE_SNOWY_HOUSE,
            BuiltInLootTables.VILLAGE_SAVANNA_HOUSE);

    /** Village house chests, where washing machines (and the book) turn up. */
    private static final Set<ResourceKey<LootTable>> HOUSE_TABLES = Set.of(
            BuiltInLootTables.VILLAGE_DESERT_HOUSE,
            BuiltInLootTables.VILLAGE_PLAINS_HOUSE,
            BuiltInLootTables.VILLAGE_TAIGA_HOUSE,
            BuiltInLootTables.VILLAGE_SNOWY_HOUSE,
            BuiltInLootTables.VILLAGE_SAVANNA_HOUSE);

    public static void register() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            // Sunken ships almost always carry "Tekef Yavo Shekemist" (~95%).
            if (key.equals(BuiltInLootTables.SHIPWRECK_TREASURE)) {
                tableBuilder.withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(ModItems.MUSIC_DISC_TEKEF_YAVO_SHEKEMIST).setWeight(19))
                        .add(EmptyLootItem.emptyItem().setWeight(1)));
            }
            // Villages occasionally hide the "Fishland Anthem" (~10%).
            if (VILLAGE_TABLES.contains(key)) {
                tableBuilder.withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(ModItems.MUSIC_DISC_FISHLAND_ANTHEM).setWeight(1))
                        .add(EmptyLootItem.emptyItem().setWeight(9)));
            }
            // Roughly half of village houses come with a washing machine to place.
            if (HOUSE_TABLES.contains(key)) {
                tableBuilder.withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(ModBlocks.WASHING_MACHINE).setWeight(1))
                        .add(EmptyLootItem.emptyItem().setWeight(1)));
            }
        });
    }
}
