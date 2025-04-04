package org.solstice.euclidsElements;

import net.minecraft.util.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solstice.euclidsElements.registry.*;

@Mod(EuclidsElements.MOD_ID)
public class EuclidsElements {

    public static final String MOD_ID = "euclids_elements";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier of(String path) {
        return Identifier.of(MOD_ID, path);
    }

    public EuclidsElements(IEventBus bus) {
		EuclidsComponentTypes.REGISTRY.register(bus);
		EuclidsEnchantmentEffects.REGISTRY.register(bus);
		VanillaTradeOfferTypes.REGISTRY.register(bus);
	}

}
