package org.solstice.euclidsElements.mapTag.mixin;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import org.solstice.euclidsElements.mapTag.api.MapTagKey;
import org.solstice.euclidsElements.mapTag.api.registry.MapTagRegistryHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(SimpleRegistry.class)
public class SimpleRegistryMixin<T> implements MapTagRegistryHolder<T> {

	@Unique private final Map<MapTagKey<T, ?>, Map<RegistryKey<T>, ?>> dataMaps = new HashMap<>();

	@Override
	public Map<MapTagKey<T, ?>, Map<RegistryKey<T>, ?>> getMapTags() {
		return this.dataMaps;
	}

}
