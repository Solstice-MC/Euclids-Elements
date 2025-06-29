package org.solstice.euclidsElements.enchantmentTooltips.mixin.enchantmentEffect;

import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.text.Text;
import org.solstice.euclidsElements.enchantmentTooltips.api.EuclidsEnchantmentEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(EnchantmentEffectEntry.class)
public abstract class EnchantmentEffectEntryMixin<T> implements EuclidsEnchantmentEffect {

	@Shadow @Final private T effect;
	@Shadow @Final private Optional<LootCondition> requirements;

	@Override
	public Text getValue(int level) {
		if (this.effect instanceof EuclidsEnchantmentEffect tooltipEffect) return tooltipEffect.getValue(level);
		return EuclidsEnchantmentEffect.super.getValue(level);
	}

	@Override
	public Text getRequirements() {
		if (this.requirements.isPresent()) return requirements.get().getDescription();
		return EuclidsEnchantmentEffect.super.getRequirements();
	}

}
