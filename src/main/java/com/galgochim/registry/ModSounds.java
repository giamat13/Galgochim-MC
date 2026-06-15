package com.galgochim.registry;

import com.galgochim.Galgochim;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public final class ModSounds {
    private ModSounds() {
    }

    public static final SoundEvent MUSIC_DISC_GALGOCHIM_BASTAV = register("music_disc.galgochim_bastav");
    public static final SoundEvent MUSIC_DISC_TEKEF_YAVO_SHEKEMIST = register("music_disc.tekef_yavo_shekemist");
    public static final SoundEvent MUSIC_DISC_FISHLAND_ANTHEM = register("music_disc.fishland_anthem");

    /** A 1% chance on any hit (Wilhelm scream). Placeholder audio - see SOUNDS_TODO.md. */
    public static final SoundEvent WILHELM_SCREAM = register("entity.wilhelm_scream");
    /** "The captain said" - heard from the Hagit spaceship. Placeholder audio - see SOUNDS_TODO.md. */
    public static final SoundEvent AMAR_HAKAPTAN = register("entity.amar_hakaptan");

    private static SoundEvent register(String path) {
        Identifier id = Galgochim.id(path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    /** Triggers static init (registration). */
    public static void register() {
    }
}
