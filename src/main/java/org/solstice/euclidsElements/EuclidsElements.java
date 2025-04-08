package org.solstice.euclidsElements;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solstice.euclidsElements.registry.*;

@Mod(EuclidsElements.MOD_ID)
public class EuclidsElements {

    public static final String MOD_ID = "euclids_elements";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public EuclidsElements(IEventBus bus) {
		EuclidsComponents.REGISTRY.register(bus);
		org.solstice.euclidsElements.registry.EuclidsEnchantmentEffects.REGISTRY.register(bus);
		VanillaTradeOfferTypes.REGISTRY.register(bus);
	}

}
