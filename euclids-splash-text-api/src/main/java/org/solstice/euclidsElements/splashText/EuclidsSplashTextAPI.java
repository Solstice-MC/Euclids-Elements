package org.solstice.euclidsElements.splashText;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import org.solstice.euclidsElements.splashText.api.loader.SplashTextLoader;

public class EuclidsSplashTextAPI implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SplashTextLoader());
	}

}
