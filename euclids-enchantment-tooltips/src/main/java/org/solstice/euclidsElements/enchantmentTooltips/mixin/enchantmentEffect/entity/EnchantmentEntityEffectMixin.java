package org.solstice.euclidsElements.enchantmentTooltips.mixin.enchantmentEffect.entity;

import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import org.solstice.euclidsElements.enchantmentTooltips.api.wip.TooltipEnchantmentEntityEffect;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnchantmentEntityEffect.class)
public interface EnchantmentEntityEffectMixin extends TooltipEnchantmentEntityEffect {}
