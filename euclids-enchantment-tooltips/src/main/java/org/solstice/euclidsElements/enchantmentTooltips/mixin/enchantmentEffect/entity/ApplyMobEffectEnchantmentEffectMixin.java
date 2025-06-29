package org.solstice.euclidsElements.enchantmentTooltips.mixin.enchantmentEffect.entity;

import com.mojang.serialization.MapCodec;
import net.minecraft.component.Component;
import net.minecraft.enchantment.effect.entity.ApplyMobEffectEnchantmentEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.text.Text;
import org.solstice.euclidsElements.enchantmentTooltips.api.EuclidsEnchantmentEffect;
import org.solstice.euclidsElements.util.EuclidsTextUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ApplyMobEffectEnchantmentEffect.class)
public abstract class ApplyMobEffectEnchantmentEffectMixin implements EuclidsEnchantmentEffect {

	@Shadow @Final private RegistryEntryList<StatusEffect> toApply;

	@Shadow public abstract MapCodec<ApplyMobEffectEnchantmentEffect> getCodec();

	@Override
	public <T> String getTranslationKey(Component<T> component) {
		return Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE.getEntry(this.getCodec())
			.getKey().orElseThrow().getValue().toTranslationKey("enchantment_entity_effect");
	}

	@Override
	public Text getValue(int level) {
		return EuclidsTextUtils.translateEntry(this.toApply, "effect");
	}

}
