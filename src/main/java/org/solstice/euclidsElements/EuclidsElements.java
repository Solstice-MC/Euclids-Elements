package org.solstice.euclidsElements;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solstice.euclidsElements.api.effectHolder.EffectHolderHelper;
import org.solstice.euclidsElements.api.event.ExtraServerLifecycleEvents;
import org.solstice.euclidsElements.registry.EuclidsComponentTypes;
import org.solstice.euclidsElements.registry.EuclidsEnchantmentEffects;
import org.solstice.euclidsElements.registry.EuclidsRegistries;

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
		EuclidsRegistries.init();
//		EuclidsComponentTypes.REGISTRY.register(bus);
//		EuclidsEnchantmentEffects.REGISTRY.register(bus);
//		VanillaTradeOfferTypes.REGISTRY.register(bus);
		ExtraServerLifecycleEvents.AFTER_RESOURCES_LOADED.register(EffectHolderHelper::initializeEffectHolderComponents);
	}

}
