package org.solstice.euclidsElements.tag.mixin;

import net.minecraft.registry.entry.RegistryEntry;
import org.solstice.euclidsElements.tag.api.registry.MapTagRegistryEntryHolder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RegistryEntry.class)
public interface RegistryEntryMixin<T> extends MapTagRegistryEntryHolder<T> {}
