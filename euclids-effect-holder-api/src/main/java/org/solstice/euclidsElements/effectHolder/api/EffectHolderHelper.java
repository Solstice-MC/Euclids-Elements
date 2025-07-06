package org.solstice.euclidsElements.effectHolder.api;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.effectHolder.api.component.EffectHolderComponent;
import org.solstice.euclidsElements.util.RegistryHelper;

import java.util.*;
import java.util.function.Predicate;

public abstract class EffectHolderHelper {

	public static final TagKey<ComponentType<?>> EFFECT_HOLDER_TAG_KEY =
		TagKey.of(RegistryKeys.DATA_COMPONENT_TYPE, EuclidsElements.of("effect_holder"));

    public static final List<ComponentType<EffectHolderComponent<? extends EffectHolder>>> EFFECT_HOLDER_COMPONENTS = new ArrayList<>();

	@SuppressWarnings("unchecked")
	public static void initializeEffectHolderComponents(MinecraftServer server) {
		RegistryWrapper.WrapperLookup lookup = server.getRegistryManager();
		RegistryEntryList<ComponentType<?>> entries = RegistryHelper.getTagValues(
			lookup,
			RegistryKeys.DATA_COMPONENT_TYPE,
			EFFECT_HOLDER_TAG_KEY
		);

		EFFECT_HOLDER_COMPONENTS.clear();
		entries.forEach(entry -> {
			ComponentType<?> component = entry.value();
			try {
				ComponentType<EffectHolderComponent<?>> holder = (ComponentType<EffectHolderComponent<?>>) component;
				EFFECT_HOLDER_COMPONENTS.add(holder);
			} catch (ClassCastException ignored) {}
		});
	}

	@SuppressWarnings("unchecked")
	public static <T extends EffectHolder> void forEachEffectHolder(ItemStack stack, Consumer<T> consumer) {
		EFFECT_HOLDER_COMPONENTS.forEach((ComponentType<?> componentType) -> {
			List<EffectHolderComponent<T>> components = getComponents((ComponentType<EffectHolderComponent<T>>) componentType, stack);
			components.stream()
				.map(EffectHolderComponent::getEffects)
				.forEach(effects ->
					effects.forEach(consumer::accept)
				);
		});
    }

	@SuppressWarnings("unchecked")
	public static <T extends EffectHolder> void forEachEffectHolder(ItemStack stack, EquipmentSlot slot, LivingEntity entity, ContextAwareConsumer<T> consumer) {
		EnchantmentEffectContext context = new EnchantmentEffectContext(stack, slot, entity);
		EFFECT_HOLDER_COMPONENTS.forEach((ComponentType<?> componentType) -> {
			List<EffectHolderComponent<T>> entityComponents = getComponents((ComponentType<EffectHolderComponent<T>>) componentType, entity);
			entityComponents.stream()
				.map(EffectHolderComponent::getEffects)
				.forEach(effects ->
					effects.forEach((entry, level) -> consumer.accept(entry, level, context))
				);
			List<EffectHolderComponent<T>> itemComponents = getComponents((ComponentType<EffectHolderComponent<T>>) componentType, stack);
			itemComponents.stream()
				.map(EffectHolderComponent::getEffects)
				.forEach(effects ->
					effects.forEach((entry, level) -> {
						if (entry.value().getDefinition().matches(context)) consumer.accept(entry, level, context);
					})
				);
		});
	}

	@SuppressWarnings("unchecked")
	public static <T extends EffectHolder> void forEachEffectHolder(ItemStack stack, EquipmentSlot slot, Consumer<T> consumer) {
		EnchantmentEffectContext context = new EnchantmentEffectContext(stack, slot, null);
		EFFECT_HOLDER_COMPONENTS.forEach((ComponentType<?> componentType) -> {
			List<EffectHolderComponent<T>> components = getComponents((ComponentType<EffectHolderComponent<T>>) componentType, stack);
			components.stream()
				.map(EffectHolderComponent::getEffects)
				.forEach(effects ->
					effects.forEach((entry, level) -> {
						if (entry.value().getDefinition().matches(context))
							consumer.accept(entry, level);
					})
				);
		});
	}

	@SuppressWarnings("unchecked")
	public static <T extends EffectHolder> void forEachEffectHolder(LivingEntity entity, ContextAwareConsumer<T> consumer) {
		EFFECT_HOLDER_COMPONENTS.forEach((ComponentType<?> componentType) -> {
			EnchantmentEffectContext entityContext = new EnchantmentEffectContext(null, null, entity);
			List<EffectHolderComponent<T>> entityComponents = getComponents((ComponentType<EffectHolderComponent<T>>) componentType, entity);
			entityComponents.stream()
				.map(EffectHolderComponent::getEffects)
				.forEach(effects ->
					effects.forEach((entry, level) -> consumer.accept(entry, level, entityContext))
				);
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				ItemStack stack = entity.getEquippedStack(slot);
				EnchantmentEffectContext context = new EnchantmentEffectContext(stack, slot, entity);
				List<EffectHolderComponent<T>> itemComponents = getComponents((ComponentType<EffectHolderComponent<T>>) componentType, stack);
				itemComponents.stream()
					.map(EffectHolderComponent::getEffects)
					.forEach(effects ->
						effects.forEach((entry, level) -> {
							if (entry.value().getDefinition().matches(context))
								consumer.accept(entry, level, context);
						})
					);
			}
		});
	}

	@SafeVarargs
	public static <T extends EffectHolder, R extends ComponentHolder> List<EffectHolderComponent<T>> getComponents(
		ComponentType<EffectHolderComponent<T>> componentType,
		R... componentHolders
	) {
		return getComponents(componentType, Arrays.stream(componentHolders).toList());
	}

	public static <T extends EffectHolder, R extends ComponentHolder> List<EffectHolderComponent<T>> getComponents(
		ComponentType<EffectHolderComponent<T>> componentType,
		List<R> componentHolders
	) {
		return componentHolders.stream()
			.filter(Objects::nonNull)
			.map(holder -> holder.get(componentType))
			.filter(Objects::nonNull)
			.toList();
	}

	public static float getEquipmentDropChance(ServerWorld world, LivingEntity target, DamageSource source, float base, Operation<Float> original) {
		MutableFloat result = new MutableFloat(base);

		Random random = target.getRandom();
		EffectHolderHelper.forEachEffectHolder(target, (effectHolder, level, context) -> {
			LootContext lootcontext = Enchantment.createEnchantedDamageLootContext(world, level, target, source);
			effectHolder.value().getEffect(EnchantmentEffectComponentTypes.EQUIPMENT_DROPS).forEach((effect) -> {
				if (effect.enchanted() == EnchantmentEffectTarget.VICTIM && effect.affected() == EnchantmentEffectTarget.VICTIM && effect.test(lootcontext))
					result.setValue(effect.effect().apply(level, random, result.floatValue()));
			});
		});
		Entity attacker = source.getAttacker();
		if (attacker instanceof LivingEntity livingAttacker) {
			EffectHolderHelper.forEachEffectHolder(livingAttacker, (effectHolder, level, context) -> {
				LootContext lootcontext = Enchantment.createEnchantedDamageLootContext(world, level, target, source);
				effectHolder.value().getEffect(EnchantmentEffectComponentTypes.EQUIPMENT_DROPS).forEach((effect) -> {
					if (effect.enchanted() == EnchantmentEffectTarget.ATTACKER && effect.affected() == EnchantmentEffectTarget.VICTIM && effect.test(lootcontext))
						result.setValue(effect.effect().apply(level, random, result.floatValue()));
				});
			});
		}

		return result.floatValue();
	}

	public static Optional<EnchantmentEffectContext> chooseEquipmentWith(ComponentType<?> componentType, LivingEntity entity, Predicate<ItemStack> stackPredicate, Operation<Optional<EnchantmentEffectContext>> original) {
		List<EnchantmentEffectContext> result = new ArrayList<>();

		for (EquipmentSlot slot : EquipmentSlot.values()) {
			ItemStack stack = entity.getEquippedStack(slot);
			if (!stackPredicate.test(stack)) continue;

			EnchantmentEffectContext context = new EnchantmentEffectContext(stack, slot, entity);
			EffectHolderHelper.forEachEffectHolder(stack, slot, (effectHolder, level) -> {
				if (effectHolder.value().getEffects().contains(componentType) && effectHolder.value().getDefinition().matches(context))
					result.add(new EnchantmentEffectContext(stack, slot, entity));
			});
		}

		return Util.getRandomOrEmpty(result, entity.getRandom());
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
