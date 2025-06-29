package org.solstice.euclidsElements;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solstice.euclidsElements.api.event.EuclidsServerEvents;

public class EuclidsElements implements ModInitializer {

	public static final String MOD_ID = "euclids_elements";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier of(String path) {
        return Identifier.of(MOD_ID, path);
    }

	public static boolean isModLoaded(String mod) {
		return FabricLoader.getInstance().isModLoaded(mod);
	}

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, m, s) ->
			EuclidsServerEvents.AFTER_RESOURCES_LOADED.invoker().afterResourcesReloaded(server)
		);
	}

}
