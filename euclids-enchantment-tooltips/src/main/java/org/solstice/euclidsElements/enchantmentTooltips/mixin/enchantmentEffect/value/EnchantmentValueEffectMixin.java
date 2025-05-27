package org.solstice.euclidsElements.enchantmentTooltips.mixin.enchantmentEffect.value;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.enchantment.effect.value.MultiplyEnchantmentEffect;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import org.solstice.euclidsElements.enchantmentTooltips.api.EuclidsEnchantmentEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EnchantmentValueEffect.class)
public interface EnchantmentValueEffectMixin extends EuclidsEnchantmentEffect {

	@Shadow float apply(int level, Random random, float inputValue);

	@Shadow
	MapCodec<? extends EnchantmentValueEffect> getCodec();

	@Override
	default Text getValue(int level) {
		float defaultValue = 0;
		if ((EnchantmentValueEffect)this instanceof MultiplyEnchantmentEffect) defaultValue = 1;

		String key = Registries.ENCHANTMENT_VALUE_EFFECT_TYPE.getEntry(this.getCodec())
			.getKey().orElseThrow().getValue().toTranslationKey("enchantment_value_effect");
		float value = this.apply(level, Random.create(), defaultValue);
		return Text.translatable(key, String.valueOf(value));
	}

}
