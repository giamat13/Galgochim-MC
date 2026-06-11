package com.galgochim.registry;

import java.util.function.Function;

import com.galgochim.Galgochim;
import com.galgochim.item.ChronoItem;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;

public final class ModItems {
    private ModItems() {
    }

    /** Jukebox songs played by the records (defined in datapack JSONs). */
    public static final ResourceKey<JukeboxSong> GALGOCHIM_BASTAV_SONG =
            ResourceKey.create(Registries.JUKEBOX_SONG, Galgochim.id("galgochim_bastav"));
    public static final ResourceKey<JukeboxSong> TEKEF_YAVO_SHEKEMIST_SONG =
            ResourceKey.create(Registries.JUKEBOX_SONG, Galgochim.id("tekef_yavo_shekemist"));
    public static final ResourceKey<JukeboxSong> FISHLAND_ANTHEM_SONG =
            ResourceKey.create(Registries.JUKEBOX_SONG, Galgochim.id("fishland_anthem"));

    public static final Item GALGOACH_SPAWN_EGG = register("galgoach_spawn_egg",
            props -> new SpawnEggItem(props.spawnEgg(ModEntities.GALGOACH)));

    public static final Item PILAPA_SPAWN_EGG = register("pilapa_spawn_egg",
            props -> new SpawnEggItem(props.spawnEgg(ModEntities.PILAPA)));

    public static final Item KARNER_SPAWN_EGG = register("karner_spawn_egg",
            props -> new SpawnEggItem(props.spawnEgg(ModEntities.KARNER)));

    public static final Item MUSIC_DISC_GALGOCHIM_BASTAV = register("music_disc_galgochim_bastav",
            props -> new Item(props.stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(GALGOCHIM_BASTAV_SONG)));

    public static final Item MUSIC_DISC_TEKEF_YAVO_SHEKEMIST = register("music_disc_tekef_yavo_shekemist",
            props -> new Item(props.stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TEKEF_YAVO_SHEKEMIST_SONG)));

    public static final Item MUSIC_DISC_FISHLAND_ANTHEM = register("music_disc_fishland_anthem",
            props -> new Item(props.stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(FISHLAND_ANTHEM_SONG)));

    // --- Issue #1 items ---------------------------------------------------

    /** Shmamba (שממבה) - "Bamba of space". A tasty snack sold by Doron Fishler. */
    public static final Item SHMAMBA = register("shmamba",
            props -> new Item(props.food(new FoodProperties.Builder()
                    .nutrition(4).saturationModifier(0.4f).build())));

    /** Shmilki (שמילקי) - "Milky of space". Also dropped (briefly) by the Hagit. */
    public static final Item SHMILKI = register("shmilki",
            props -> new Item(props.food(new FoodProperties.Builder()
                    .nutrition(6).saturationModifier(0.6f).build())));

    /** The book "I can explain" (אני יכול להסביר), sold by Doron Fishler. */
    public static final Item BOOK_ANI_YACHOL_LEHASBIR = register("book_ani_yachol_lehasbir",
            props -> new Item(props.stacksTo(16)));

    /** Laundry powder (אבקת כביסה) - 64 of these fill a washing machine. */
    public static final Item LAUNDRY_POWDER = register("laundry_powder",
            props -> new Item(props));

    /** The chrono-thing (כרונומשהו) dropped by the Hagit when shot. */
    public static final Item CHRONO = register("chrono",
            props -> new ChronoItem(props.stacksTo(16).rarity(Rarity.RARE)));

    /** A single sock (גרב) - aliens turn leather boots into these. */
    public static final Item SOCK = register("sock",
            props -> new Item(props));

    private static Item register(String name, Function<Item.Properties, Item> factory) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Galgochim.id(name));
        Item item = factory.apply(new Item.Properties().setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    /** Triggers static init (registration) and adds items to the vanilla creative tabs. */
    public static void register() {
        // Spawn eggs go in the vanilla "Spawn Eggs" tab.
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(output -> {
            output.accept(GALGOACH_SPAWN_EGG);
            output.accept(PILAPA_SPAWN_EGG);
            output.accept(KARNER_SPAWN_EGG);
        });
        // Records go in the vanilla tab alongside the other music discs.
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output -> {
            output.accept(MUSIC_DISC_GALGOCHIM_BASTAV);
            output.accept(MUSIC_DISC_TEKEF_YAVO_SHEKEMIST);
            output.accept(MUSIC_DISC_FISHLAND_ANTHEM);
        });
        // Edible goodies go in the food tab.
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(output -> {
            output.accept(SHMAMBA);
            output.accept(SHMILKI);
        });
        // The rest of the new odds and ends.
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {
            output.accept(BOOK_ANI_YACHOL_LEHASBIR);
            output.accept(LAUNDRY_POWDER);
            output.accept(CHRONO);
            output.accept(SOCK);
        });
    }
}
