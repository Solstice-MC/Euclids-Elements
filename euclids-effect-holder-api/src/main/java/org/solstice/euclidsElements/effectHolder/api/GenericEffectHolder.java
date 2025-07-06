package org.solstice.euclidsElements.effectHolder.api;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.EnchantmentEffectContext;

import java.util.List;

public interface GenericEffectHolder extends EffectHolder {

	@Override
	default EffectHolder.Definition getDefinition() {
		return new Definition(){};
	}

	interface Definition extends EffectHolder.Definition {

		@Override
		default int getMaxLevel() {
			return 1;
		}

		@Override
		default List<AttributeModifierSlot> getSlots() {
			return List.of();
		}

		@Override
		default boolean contains(AttributeModifierSlot slot) {
			return true;
		}

		@Override
		default boolean matches(EnchantmentEffectContext context) {
			return true;
		}

    }

}
