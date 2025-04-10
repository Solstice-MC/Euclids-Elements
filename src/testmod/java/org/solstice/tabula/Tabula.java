package org.solstice.tabula;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solstice.tabula.registry.TabulaBlocks;
import org.solstice.tabula.registry.TabulaItems;

public class Tabula implements ModInitializer {

	public static final String MOD_ID = "tabula";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier of(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		TabulaItems.init();
		TabulaBlocks.init();
	}

}
