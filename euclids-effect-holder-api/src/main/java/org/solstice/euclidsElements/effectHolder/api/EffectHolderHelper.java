package org.solstice.euclidsElements.effectHolder.api;

import net.minecraft.component.ComponentType;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.effectHolder.api.component.EffectHolderComponent;
import org.solstice.euclidsElements.effectHolder.mixin.EnchantmentHelperMixin;
import org.solstice.euclidsElements.util.RegistryHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class EffectHolderHelper {

    public static final List<ComponentType<? extends EffectHolderComponent<?>>> EFFECT_HOLDER_COMPONENTS = new ArrayList<>();

	public static final TagKey<ComponentType<?>> EFFECT_HOLDER =
		TagKey.of(RegistryKeys.DATA_COMPONENT_TYPE, EuclidsElements.of("effect_holder"));

	@SuppressWarnings("unchecked")
	public static void initializeEffectHolderComponents(MinecraftServer server) {
		RegistryWrapper.WrapperLookup lookup = server.getRegistryManager();
		RegistryEntryList<ComponentType<?>> entries = RegistryHelper.getTagValues(lookup, RegistryKeys.DATA_COMPONENT_TYPE, EFFECT_HOLDER);

		EFFECT_HOLDER_COMPONENTS.clear();
		entries.forEach(entry -> {
			ComponentType<?> component = entry.value();
			try {
				ComponentType<? extends EffectHolderComponent<?>> holder = (ComponentType<? extends EffectHolderComponent<?>>) component;
				EFFECT_HOLDER_COMPONENTS.add(holder);
			} catch (ClassCastException ignored) {}
		});
	}

	@SuppressWarnings("unchecked")
	public static <T extends EffectHolder> void forEachEffectHolder(ItemStack stack, Consumer<T> consumer) {
		EFFECT_HOLDER_COMPONENTS.forEach(componentType -> {
			EffectHolderComponent<?> component = stack.getOrDefault(componentType, null);
			if (component == null) return;

			component.getEffects().forEach((entry, level) -> {
				consumer.accept((RegistryEntry<T>) entry, level);
			});
		});
    }

	@SuppressWarnings("unchecked")
	public static <T extends EffectHolder> void forEachEffectHolder(ItemStack stack, EquipmentSlot slot, LivingEntity entity, ContextAwareConsumer<T> consumer) {
		if (stack.isEmpty()) return;

		EFFECT_HOLDER_COMPONENTS.forEach(componentType -> {
			EffectHolderComponent<?> component = stack.getOrDefault(componentType, null);
			if (component == null) return;

			EnchantmentEffectContext context = new EnchantmentEffectContext(stack, slot, entity);
			component.getEffects().forEach((entry, level) -> {
				if (entry.value().slotMatches(slot)) {
					consumer.accept((RegistryEntry<T>) entry, level, context);
				}
			});
		});
    }

    public static void forEachEffectHolder(LivingEntity entity, ContextAwareConsumer<?> consumer) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            forEachEffectHolder(entity.getEquippedStack(slot), slot, entity, consumer);
        }
    }

	public static int getMaxDurability(ItemStack stack, int base) {
		MutableFloat result = new MutableFloat(base);
		forEachEffectHolder(stack, (effectHolder, level) ->
			effectHolder.value().modifyMaxDurability(level, result)
		);
		return result.intValue();
	}


    @FunctionalInterface
    public interface Consumer<T extends EffectHolder> {
        void accept(RegistryEntry<T> entry, int level);
    }

    @FunctionalInterface
    public interface ContextAwareConsumer<T extends EffectHolder> {
        void accept(RegistryEntry<T> entry, int level, EnchantmentEffectContext context);
    }

}
