package org.solstice.euclidsElements.mapTag.mixin;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import org.solstice.euclidsElements.mapTag.api.MapTagKey;
import org.solstice.euclidsElements.mapTag.api.registry.MapTagRegistryEntryHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(RegistryEntry.class)
public interface RegistryEntryMixin<T> extends MapTagRegistryEntryHolder<T> {

	@Shadow Optional<RegistryKey<T>> getKey();

	@Override
	default <R> R getValue(Registry<T> registry, MapTagKey<T, R> tag) {
		return registry.getMapTagValue(tag, this.getKey().orElseThrow());
	}

}
