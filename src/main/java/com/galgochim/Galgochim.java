package com.galgochim;

import com.galgochim.registry.ModEntities;
import com.galgochim.registry.ModItems;
import com.galgochim.registry.ModLoot;
import com.galgochim.registry.ModSounds;
import com.galgochim.registry.ModSpawns;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Galgochim implements ModInitializer {
	public static final String MOD_ID = "galgochim";

	// This logger is used to write text to the console and the log file.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	/** Build an Identifier in this mod's namespace. */
	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModSounds.register();
		ModEntities.register();
		ModItems.register();
		ModSpawns.register();
		ModLoot.register();

		LOGGER.info("Galgochim loaded: galgochim, pilapot and karnerim are roaming!");
	}
}
