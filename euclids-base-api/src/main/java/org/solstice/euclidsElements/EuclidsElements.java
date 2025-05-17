package org.solstice.euclidsElements;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solstice.euclidsElements.registry.EuclidsComponentTypes;
import org.solstice.euclidsElements.registry.EuclidsEnchantmentEffects;

public class EuclidsElements implements ModInitializer {

    public static final String MOD_ID = "euclids_elements";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier of(String path) {
        return Identifier.of(MOD_ID, path);
    }

	@Override
	public void onInitialize() {
		EuclidsComponentTypes.init();
		EuclidsEnchantmentEffects.init();
	}

}
