package org.solstice.euclidsElements.enchantmentTooltips.mixin.lootCondition;

import net.minecraft.loot.condition.AllOfLootCondition;
import net.minecraft.loot.condition.AlternativeLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.solstice.euclidsElements.enchantmentTooltips.api.EuclidsLootCondition;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;
import java.util.function.Predicate;

@Mixin(AllOfLootCondition.class)
public abstract class AllOfLootConditionMixin extends AlternativeLootCondition implements EuclidsLootCondition {

	protected AllOfLootConditionMixin(List<LootCondition> terms, Predicate<LootContext> predicate) {
		super(terms, predicate);
	}

	// TODO return fancy list of conditions with commas and stuff
	@Override
	public MutableText getDescription() {
		MutableText result = Text.empty();
		for (LootCondition condition : this.terms) {
			if (condition instanceof EuclidsLootCondition tooltipCondition) {
				result.append(tooltipCondition.getDescription()).append(" ");
			}
		}
		return result;
	}

}
