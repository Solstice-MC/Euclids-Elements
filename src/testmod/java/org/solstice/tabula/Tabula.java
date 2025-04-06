package org.solstice.tabula;

import net.minecraft.util.Identifier;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Tabula.MOD_ID)
public class Tabula {

	public static final String MOD_ID = "tabula";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier of(String path) {
		return Identifier.of(MOD_ID, path);
	}

}
