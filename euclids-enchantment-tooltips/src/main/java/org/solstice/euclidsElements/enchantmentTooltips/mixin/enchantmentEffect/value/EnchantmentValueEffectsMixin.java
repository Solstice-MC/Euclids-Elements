package org.solstice.euclidsElements.enchantmentTooltips.mixin.enchantmentEffect.value;

import net.minecraft.enchantment.effect.AllOfEnchantmentEffects;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import org.solstice.euclidsElements.enchantmentTooltips.api.EuclidsEnchantmentEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AllOfEnchantmentEffects.ValueEffects.class)
public abstract class EnchantmentValueEffectsMixin implements EuclidsEnchantmentEffect {

	@Shadow public abstract float apply(int level, Random random, float inputValue);

	// TODO make this fancier somehow
	@Override
	public Text getValue(int level) {
		float value = this.apply(level, Random.create(), 1);
		return Text.literal(String.valueOf(value));
	}

}
