package org.solstice.euclidsElements.effectHolder.api.component;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.registry.entry.RegistryEntry;
import org.solstice.euclidsElements.effectHolder.api.EffectHolder;

import java.util.List;

/**
 * A specialized effect holder component that manages effects that all have a level of 1.
 * This component simplifies the storage by maintaining just a list of effects rather than a map of effects to levels.
 *
 * @param <T> The type of effect holder this component manages, must extend {@link EffectHolder}
 */
public interface UnleveledEffectHolderComponent<T extends EffectHolder> extends EffectHolderComponent<T> {

    /**
     * Gets the list of effects managed by this component.
     * All effects in this list are implicitly at level 1.
     *
     * @return A list of registry entries for the effects
     */
	List<RegistryEntry<T>> getAllEffects();

    /**
     * Converts the list of single effects into a map where all effects have a level of 1.
     * This method overrides the parent interface method to provide the appropriate implementation.
     *
     * @return A map containing registry entries of effect holders mapped to level 1
     */
    @Override
    default Object2IntOpenHashMap<RegistryEntry<T>> getEffects() {
        Object2IntOpenHashMap<RegistryEntry<T>> result = new Object2IntOpenHashMap<>();
        this.getAllEffects().forEach(entry -> result.put(entry, 1));
        return result;
    }

}
