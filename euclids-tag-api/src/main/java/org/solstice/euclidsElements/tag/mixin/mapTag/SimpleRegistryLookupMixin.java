package org.solstice.euclidsElements.tag.mixin.mapTag;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.SimpleRegistry;
import org.solstice.euclidsElements.tag.api.MapTagKey;
import org.solstice.euclidsElements.tag.api.registry.MapTagHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(targets = "net.minecraft.registry.SimpleRegistry$1")
public abstract class SimpleRegistryLookupMixin<T> implements RegistryWrapper.Impl<T>, MapTagHolder<T> {

	@Shadow @Final SimpleRegistry<T> field_36468;

	@Override
	public Map<MapTagKey<T, ?>, Map<RegistryKey<T>, ?>> getMapTags() {
		return this.field_36468.getMapTags();
	}

}
