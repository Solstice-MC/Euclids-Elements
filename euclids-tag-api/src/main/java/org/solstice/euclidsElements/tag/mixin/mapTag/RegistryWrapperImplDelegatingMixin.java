package org.solstice.euclidsElements.tag.mixin.mapTag;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import org.solstice.euclidsElements.tag.api.MapTagKey;
import org.solstice.euclidsElements.tag.api.registry.MapTagHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(RegistryWrapper.Impl.Delegating.class)
public interface RegistryWrapperImplDelegatingMixin<T> extends RegistryWrapper.Impl<T>, MapTagHolder<T> {

	@Shadow RegistryWrapper.Impl<T> getBase();

	default Map<MapTagKey<T, ?>, Map<RegistryKey<T>, ?>> getMapTags() {
		return this.getBase().getMapTags();
	}

}
