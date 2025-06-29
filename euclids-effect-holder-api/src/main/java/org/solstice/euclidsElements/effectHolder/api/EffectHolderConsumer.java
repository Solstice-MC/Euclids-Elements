package org.solstice.euclidsElements.effectHolder.api;

import net.minecraft.registry.entry.RegistryEntry;

@FunctionalInterface
public interface EffectHolderConsumer {

    void accept(RegistryEntry<? extends EffectHolder> enchantment, int level);

}
