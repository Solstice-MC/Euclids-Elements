package org.solstice.euclidsElements.api.effectHolder;

import net.minecraft.core.Holder;

@FunctionalInterface
public interface EffectHolderConsumer {

    void accept(Holder<? extends EffectHolder> enchantment, int level);

}
