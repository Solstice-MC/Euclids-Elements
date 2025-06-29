package org.solstice.euclidsElements.tag.mixin.mapTag;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryOwner;
import org.solstice.euclidsElements.tag.api.MapTagKey;
import org.solstice.euclidsElements.tag.api.registry.MapTagAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(RegistryEntry.Reference.class)
public abstract class RegistryEntryReferenceMixin<T> implements MapTagAccessor<T> {

	@Shadow @Final private RegistryEntryOwner<T> owner;
	@Shadow private RegistryKey<Registry<T>> registryKey;

	@Override
	public Map<MapTagKey<T, ?>, Map<RegistryKey<T>, ?>> getMapTags() {
		return this.owner instanceof RegistryWrapper.Impl<T> lookup ? lookup.getMapTags() : Map.of();
	}

	@Override
	public RegistryKey<Registry<T>> getRegistryKey() {
		return this.registryKey;
	}

}
