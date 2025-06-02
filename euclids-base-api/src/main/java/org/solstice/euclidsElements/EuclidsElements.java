package org.solstice.euclidsElements;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EuclidsElements {

    public static final String MOD_ID = "euclids_elements";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier of(String path) {
        return Identifier.of(MOD_ID, path);
    }

	public static boolean isModLoaded(String mod) {
		return FabricLoader.getInstance().isModLoaded(mod);
	}

}
