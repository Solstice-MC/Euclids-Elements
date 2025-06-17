package org.solstice.euclidsElements.tag.mixin;

import net.minecraft.registry.entry.RegistryEntry;
import org.solstice.euclidsElements.tag.api.MapTagKey;
import org.solstice.euclidsElements.tag.api.registry.MapTagRegistryEntryHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(RegistryEntry.Reference.class)
public abstract class RegistryEntryReferenceMixin<T> implements RegistryEntry<T>, MapTagRegistryEntryHolder<T> {

	@Unique
	private final Map<MapTagKey<T, Object>, Object> dataMaps = new HashMap<>();

	@Override
	public Map<MapTagKey<T, Object>, Object> getMapTags() {
		return this.dataMaps;
	}

}
