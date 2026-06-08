package com.galgochim.registry;

import java.util.function.Function;

import com.galgochim.Galgochim;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
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
    }
}
