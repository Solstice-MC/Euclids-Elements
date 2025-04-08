package org.solstice.euclidsElements.api.effectHolder.item.component;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Holder;
import org.solstice.euclidsElements.api.effectHolder.EffectHolder;

import java.util.List;

public interface SingleEffectHolderComponent<T extends EffectHolder> extends EffectHolderComponent<T> {

	List<Holder<T>> getSingleEffects();

    @Override
    default Object2IntOpenHashMap<Holder<T>> getEffects() {
        Object2IntOpenHashMap<Holder<T>> result = new Object2IntOpenHashMap<>();
        this.getSingleEffects().forEach(entry -> result.put(entry, 1));
        return result;
    }

}
