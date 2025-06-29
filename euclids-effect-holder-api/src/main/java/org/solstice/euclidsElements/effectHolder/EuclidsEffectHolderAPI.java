package org.solstice.euclidsElements.effectHolder;

import net.fabricmc.api.ModInitializer;
import org.solstice.euclidsElements.api.event.EuclidsServerEvents;
import org.solstice.euclidsElements.effectHolder.api.EffectHolderHelper;
import org.solstice.euclidsElements.effectHolder.registry.EuclidsEnchantmentEffects;

public class EuclidsEffectHolderAPI implements ModInitializer {

	@Override
	public void onInitialize() {
		EuclidsEnchantmentEffects.init();
		EuclidsServerEvents.AFTER_RESOURCES_LOADED.register(EffectHolderHelper::initializeEffectHolderComponents);
	}

}
