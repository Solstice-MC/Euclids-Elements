package org.solstice.euclidsElements.enchantmentTooltips.mixin.lootCondition;

import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.solstice.euclidsElements.enchantmentTooltips.api.EuclidsLootCondition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(EntityPropertiesLootCondition.class)
public abstract class EntityPropertiesLootConditionMixin implements LootCondition, EuclidsLootCondition {

	@Shadow public abstract LootConditionType getType();

	@Shadow @Final private Optional<EntityPredicate> predicate;

	@Override
	public MutableText getDescription() {
		if (this.predicate.isEmpty()) return LootCondition.super.getDescription();
		return Text.translatable(this.getTranslationKey(), EuclidsLootCondition.translatePredicate(this.predicate.get()));
	}

}
