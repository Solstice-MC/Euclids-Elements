package org.solstice.euclidsElements.mixin;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.enchantment.Enchantment;
import org.solstice.euclidsElements.api.effectHolder.EffectHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Enchantment.class)
public class EnchantmentMixin implements EffectHolder {

    @Shadow @Final private DataComponentMap effects;
    @Shadow @Final private Enchantment.EnchantmentDefinition definition;

    @Override
    public DataComponentMap getEffects() {
        return this.effects;
    }

    @Override
    public Definition getDefinition() {
        return this.definition;
    }

}
