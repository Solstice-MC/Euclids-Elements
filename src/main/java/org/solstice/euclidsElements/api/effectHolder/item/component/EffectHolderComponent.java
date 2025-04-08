package org.solstice.euclidsElements.api.effectHolder.item.component;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Holder;
import org.solstice.euclidsElements.api.effectHolder.EffectHolder;

import java.util.Collections;
import java.util.Set;

public interface EffectHolderComponent<T extends EffectHolder> {

    Object2IntOpenHashMap<Holder<T>> getEffects();

    default int getLevel(Holder<T> effect) {
        return this.getEffects().getInt(effect);
    }

    default Set<Object2IntMap.Entry<Holder<T>>> entrySet() {
        return Collections.unmodifiableSet(this.getEffects().object2IntEntrySet());
    }

    default int size() {
        return this.getEffects().size();
    }

    default boolean isEmpty() {
        return this.getEffects().isEmpty();
    }

}
