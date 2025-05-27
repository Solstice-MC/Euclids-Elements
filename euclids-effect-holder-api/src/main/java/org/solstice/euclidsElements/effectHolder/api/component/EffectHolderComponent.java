package org.solstice.euclidsElements.effectHolder.api.component;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.registry.entry.RegistryEntry;
import org.solstice.euclidsElements.effectHolder.api.EffectHolder;

import java.util.Collections;
import java.util.Set;

/**
 * Component type that stores {@link EffectHolder}s.
 *
 * @param <T> The type of effect holder this component manages, must extend {@link EffectHolder}.
 */
public interface EffectHolderComponent<T extends EffectHolder> {

    Object2IntOpenHashMap<RegistryEntry<T>> getEffects();

    /**
     * Gets the level of a specific effect holder.
     *
     * @param effect The registry entry of the effect holder to get the level for
     * @return The level of the effect, or 0 if the effect is not present
     */
    default int getLevel(RegistryEntry<T> effect) {
        return this.getEffects().getOrDefault(effect, 0);
    }

    default Set<Object2IntMap.Entry<RegistryEntry<T>>> entrySet() {
        return Collections.unmodifiableSet(this.getEffects().object2IntEntrySet());
    }

    default int size() {
        return this.getEffects().size();
    }

    default boolean isEmpty() {
        return this.getEffects().isEmpty();
    }

}
