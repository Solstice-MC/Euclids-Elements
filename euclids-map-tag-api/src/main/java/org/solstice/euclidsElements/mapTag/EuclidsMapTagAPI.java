package org.solstice.euclidsElements.mapTag;

import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.DataPackContents;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.mapTag.api.MapTagKey;
import org.solstice.euclidsElements.mapTag.api.MapTagLoader;

public class EuclidsMapTagAPI implements ModInitializer {

	public static final MapTagKey<Block, Integer> TEST = MapTagKey.of(RegistryKeys.BLOCK, Codec.INT, EuclidsElements.of("test"));

	@Override
	public void onInitialize() {
		System.out.println("AAAAAAAAAAAAAAAAAAAAA");
		System.out.println(TEST);
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(MapTagLoader.ID, registries ->
			new MapTagLoader<>(((DataPackContents.ConfigurableWrapperLookup)registries).dynamicRegistryManager)
		);
	}

}
