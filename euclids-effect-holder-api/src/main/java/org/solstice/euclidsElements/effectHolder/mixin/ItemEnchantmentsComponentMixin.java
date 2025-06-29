package org.solstice.euclidsElements.effectHolder.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import org.solstice.euclidsElements.effectHolder.api.component.EffectHolderComponent;
import org.spongepowered.asm.mixin.*;

@Mixin(ItemEnchantmentsComponent.class)
public abstract class ItemEnchantmentsComponentMixin implements EffectHolderComponent<Enchantment> {

    @Shadow @Final Object2IntOpenHashMap<RegistryEntry<Enchantment>> enchantments;

	@Override
    public Object2IntOpenHashMap<RegistryEntry<Enchantment>> getEffects() {
        return this.enchantments;
    }

}
