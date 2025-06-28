package org.solstice.euclidsElements.content;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solstice.euclidsElements.content.registry.*;

public class EuclidsContent implements ModInitializer {

	@Override
	public void onInitialize() {
		EuclidsComponentTypes.init();
		EuclidsPackets.init();
		EuclidsTags.init();
	}

}
