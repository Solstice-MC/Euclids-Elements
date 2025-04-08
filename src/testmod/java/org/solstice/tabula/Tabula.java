package org.solstice.tabula;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Tabula.MOD_ID)
public class Tabula {

	public static final String MOD_ID = "tabula";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ResourceLocation of(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

}
