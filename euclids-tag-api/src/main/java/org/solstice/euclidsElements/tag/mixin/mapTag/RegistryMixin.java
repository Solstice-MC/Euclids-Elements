package org.solstice.euclidsElements.tag.mixin.mapTag;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.solstice.euclidsElements.tag.api.registry.MapTagAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Registry.class)
public interface RegistryMixin<T> extends MapTagAccessor<T> {

	@Shadow RegistryKey<? extends Registry<T>> getKey();

	@Override
	@SuppressWarnings("unchecked")
	default RegistryKey<Registry<T>> getRegistryKey() {
		return (RegistryKey<Registry<T>>) this.getKey();
	}

}
