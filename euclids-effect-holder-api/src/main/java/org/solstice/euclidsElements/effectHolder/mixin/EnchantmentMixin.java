package org.solstice.euclidsElements.effectHolder.mixin;

import net.minecraft.component.ComponentMap;
import net.minecraft.enchantment.Enchantment;
import org.solstice.euclidsElements.effectHolder.api.EffectHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Enchantment.class)
public class EnchantmentMixin implements EffectHolder {

    @Shadow @Final private ComponentMap effects;
    @Shadow @Final private Enchantment.Definition definition;

	@Override
    public ComponentMap getEffects() {
        return this.effects;
    }

    @Override
    public Definition getDefinition() {
        return this.definition;
    }

}
