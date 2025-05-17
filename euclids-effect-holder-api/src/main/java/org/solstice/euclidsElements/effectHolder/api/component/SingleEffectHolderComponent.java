package org.solstice.euclidsElements.effectHolder.api.component;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.registry.entry.RegistryEntry;
import org.solstice.euclidsElements.effectHolder.api.EffectHolder;

import java.util.List;

public interface SingleEffectHolderComponent<T extends EffectHolder> extends EffectHolderComponent<T> {

	List<RegistryEntry<T>> getSingleEffects();

    @Override
    default Object2IntOpenHashMap<RegistryEntry<T>> getEffects() {
        Object2IntOpenHashMap<RegistryEntry<T>> result = new Object2IntOpenHashMap<>();
        this.getSingleEffects().forEach(entry -> result.put(entry, 1));
        return result;
    }

}
