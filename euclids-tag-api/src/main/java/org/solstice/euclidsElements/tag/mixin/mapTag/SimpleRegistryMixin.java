package org.solstice.euclidsElements.tag.mixin.mapTag;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import org.solstice.euclidsElements.tag.api.MapTagKey;
import org.solstice.euclidsElements.tag.api.registry.MapTagHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.IdentityHashMap;
import java.util.Map;

@Mixin(SimpleRegistry.class)
public class SimpleRegistryMixin<T> implements MapTagHolder<T> {

	@Unique final Map<MapTagKey<T, ?>, Map<RegistryKey<T>, ?>> mapTags = new IdentityHashMap<>();

	@Override
	public Map<MapTagKey<T, ?>, Map<RegistryKey<T>, ?>> getMapTags() {
		return this.mapTags;
	}

}
