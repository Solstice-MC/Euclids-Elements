package org.solstice.euclidsElements.enchantmentTooltips.api;

import net.minecraft.loot.condition.LootCondition;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.solstice.euclidsElements.util.EuclidsTextUtils;

public interface EuclidsLootCondition {

	default String getTranslationKey() {
		return Registries.LOOT_CONDITION_TYPE.getEntry(((LootCondition)this).getType())
			.getKey().orElseThrow().getValue().toTranslationKey("loot_condition");
	}

	default MutableText getDescription() {
		return Text.translatable(this.getTranslationKey());
	}

	static Text translatePredicate(EntityPredicate predicate) {
		if (predicate.type().isPresent()) {
			return EuclidsTextUtils.translateEntry(predicate.type().get().types());
		}
		return Text.empty();
	}

}
