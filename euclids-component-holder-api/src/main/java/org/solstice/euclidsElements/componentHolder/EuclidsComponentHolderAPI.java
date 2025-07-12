package org.solstice.euclidsElements.componentHolder;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import org.solstice.euclidsElements.componentHolder.core.DefaultComponentLoader;
import org.solstice.euclidsElements.componentHolder.registry.EuclidsCommands;
import org.solstice.euclidsElements.componentHolder.registry.EuclidsTrackedDataHandlers;

import java.util.concurrent.atomic.AtomicReference;

public class EuclidsComponentHolderAPI implements ModInitializer {

	@Override
	public void onInitialize() {
		AtomicReference<DefaultComponentLoader> loaderReference = new AtomicReference<>();
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(DefaultComponentLoader.ID, registries -> {
			DefaultComponentLoader loader = new DefaultComponentLoader(registries);
			loaderReference.set(loader);
			return loader;
		});
		CommonLifecycleEvents.TAGS_LOADED.register((registries, client) ->
			loaderReference.get().apply(registries, client)
		);

		EuclidsTrackedDataHandlers.init();
		EuclidsCommands.init();
	}

}
