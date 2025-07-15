package org.solstice.euclidsElements.tag.mixin.mapTag;

import net.minecraft.registry.RegistryWrapper;
import org.solstice.euclidsElements.tag.api.registry.MapTagHolder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RegistryWrapper.Impl.class)
public interface RegistryWrapperImplMixin<T> extends MapTagHolder<T> {}
