package org.solstice.tabula;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solstice.tabula.registry.*;

@Mod(Tabula.MOD_ID)
public class Tabula {

	public static final String MOD_ID = "tabula";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ResourceLocation of(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public Tabula (IEventBus bus) {
		TabulaItems.REGISTRY.register(bus);
		TabulaBlocks.REGISTRY.register(bus);
	}

}
