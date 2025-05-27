package org.solstice.euclidsElements.effectHolder;

import net.fabricmc.api.ModInitializer;
import org.solstice.euclidsElements.api.event.EuclidsServerEvents;
import org.solstice.euclidsElements.effectHolder.api.EffectHolderHelper;

public class EuclidsEffectHolderAPI implements ModInitializer {

	@Override
	public void onInitialize() {
		EuclidsServerEvents.AFTER_RESOURCES_LOADED.register(EffectHolderHelper::initializeEffectHolderComponents);
	}

}
