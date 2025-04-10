package org.solstice.euclidsElements.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import org.solstice.euclidsElements.api.effectHolder.item.component.EffectHolderComponent;
import org.spongepowered.asm.mixin.*;

@Mixin(ItemEnchantmentsComponent.class)
public class ItemEnchantmentsComponentMixin implements EffectHolderComponent<Enchantment> {

    @Shadow @Final Object2IntOpenHashMap<RegistryEntry<Enchantment>> enchantments;

    @Override
    public Object2IntOpenHashMap<RegistryEntry<Enchantment>> getEffects() {
        return this.enchantments;
    }

}
