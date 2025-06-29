package org.solstice.euclidsElements.componentHolder;

import net.fabricmc.api.ModInitializer;
import org.solstice.euclidsElements.componentHolder.registry.EuclidsTrackedDataHandlers;

public class EuclidsComponentHolderAPI implements ModInitializer {

	@Override
	public void onInitialize() {
		EuclidsTrackedDataHandlers.init();
	}

}
