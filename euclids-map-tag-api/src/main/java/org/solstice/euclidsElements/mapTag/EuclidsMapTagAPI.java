package org.solstice.euclidsElements.mapTag;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.DataPackContents;
import org.solstice.euclidsElements.mapTag.api.MapTagLoader;

public class EuclidsMapTagAPI implements ModInitializer {

	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(MapTagLoader.ID, registries ->
			new MapTagLoader<>(((DataPackContents.ConfigurableWrapperLookup)registries).dynamicRegistryManager)
		);
	}

}
