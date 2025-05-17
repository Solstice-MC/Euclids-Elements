package org.solstice.euclidsElements.effectHolder;

import net.fabricmc.api.ModInitializer;
import org.solstice.euclidsElements.api.event.ExtraServerLifecycleEvents;
import org.solstice.euclidsElements.effectHolder.api.EffectHolderHelper;

public class EuclidsEffectHolderAPI implements ModInitializer {

	@Override
	public void onInitialize() {
		ExtraServerLifecycleEvents.AFTER_RESOURCES_LOADED.register(EffectHolderHelper::initializeEffectHolderComponents);
	}

}
