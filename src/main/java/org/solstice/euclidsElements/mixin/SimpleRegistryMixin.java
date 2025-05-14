package org.solstice.euclidsElements.mixin;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import org.solstice.euclidsElements.api.mapTag.MapTagKey;
import org.solstice.euclidsElements.api.mapTag.registry.MapTagRegistryHolder;
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
