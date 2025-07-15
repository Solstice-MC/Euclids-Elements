package org.solstice.euclidsElements.tag.mixin.mapTag;

import net.minecraft.registry.entry.RegistryEntry;
import org.solstice.euclidsElements.tag.api.registry.MapTagAccessor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RegistryEntry.class)
public interface RegistryEntryMixin<T> extends MapTagAccessor<T> {}
