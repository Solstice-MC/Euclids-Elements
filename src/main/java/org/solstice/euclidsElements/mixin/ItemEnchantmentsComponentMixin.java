package org.solstice.euclidsElements.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.solstice.euclidsElements.api.effectHolder.item.component.EffectHolderComponent;
import org.spongepowered.asm.mixin.*;

@Mixin(ItemEnchantments.class)
public class ItemEnchantmentsComponentMixin implements EffectHolderComponent<Enchantment> {

    @Shadow @Final Object2IntOpenHashMap<Holder<Enchantment>> enchantments;

    @Override
    public Object2IntOpenHashMap<Holder<Enchantment>> getEffects() {
        return this.enchantments;
    }

}
