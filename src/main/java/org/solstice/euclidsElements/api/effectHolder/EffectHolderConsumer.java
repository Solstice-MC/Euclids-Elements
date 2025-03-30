package org.solstice.euclidsElements.api.effectHolder;

import net.minecraft.registry.entry.RegistryEntry;

@FunctionalInterface
public interface EffectHolderConsumer {

    void accept(RegistryEntry<? extends EffectHolder> enchantment, int level);

}
