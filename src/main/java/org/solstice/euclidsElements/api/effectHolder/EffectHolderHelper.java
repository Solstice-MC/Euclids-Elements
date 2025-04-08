package org.solstice.euclidsElements.api.effectHolder;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.api.effectHolder.item.component.EffectHolderComponent;
import org.solstice.euclidsElements.util.RegistryHelper;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = EuclidsElements.MOD_ID)
public class EffectHolderHelper {

    public static final List<DataComponentType<? extends EffectHolderComponent<?>>> EFFECT_HOLDER_COMPONENTS = new ArrayList<>();

	public static final TagKey<DataComponentType<?>> EFFECT_HOLDER =
		TagKey.create(Registries.DATA_COMPONENT_TYPE, EuclidsElements.of("effect_holder"));

	@SubscribeEvent
	public static void onAddReloadListeners(AddReloadListenerEvent event) {
		initializeEffectHolderComponents(event.getRegistryAccess());
	}

	@SubscribeEvent
	public static void onServerAboutToStart(ServerAboutToStartEvent event) {
		initializeEffectHolderComponents(event.getServer().registryAccess().freeze());
	}

	@SuppressWarnings("unchecked")
	public static void initializeEffectHolderComponents(HolderLookup.Provider lookup) {
		HolderSet<DataComponentType<?>> entries = RegistryHelper.getTagValues(lookup, Registries.DATA_COMPONENT_TYPE, EFFECT_HOLDER);

		EFFECT_HOLDER_COMPONENTS.clear();
		entries.forEach(entry -> {
			DataComponentType<?> component = entry.value();
			try {
				DataComponentType<? extends EffectHolderComponent<?>> holder = (DataComponentType<? extends EffectHolderComponent<?>>) component;
				EFFECT_HOLDER_COMPONENTS.add(holder);
			} catch (ClassCastException ignored) {}
		});
	}

	public static void forEachEffectHolder(ItemStack stack, Consumer consumer) {
        EFFECT_HOLDER_COMPONENTS.forEach(componentType -> {
            EffectHolderComponent<?> component = stack.getOrDefault(componentType, null);
            if (component == null) return;
            component.getEffects().forEach(consumer::accept);
        });
    }

    public static void forEachEffectHolder(ItemStack stack, EquipmentSlot slot, LivingEntity entity, ContextAwareConsumer contextAwareConsumer) {
        if (stack.isEmpty()) return;

        EFFECT_HOLDER_COMPONENTS.forEach(componentType -> {
            EffectHolderComponent<?> component = stack.getOrDefault(componentType, null);
            if (component == null) return;

			EnchantedItemInUse context = new EnchantedItemInUse(stack, slot, entity);
            component.getEffects().forEach((entry, level) -> {
                if (entry.value().slotMatches(slot)) {
					contextAwareConsumer.accept(entry, level, context);
                }
            });
        });
    }

    public static void forEachEffectHolder(LivingEntity entity, ContextAwareConsumer consumer) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            forEachEffectHolder(entity.getItemBySlot(slot), slot, entity, consumer);
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
    public interface Consumer {
        void accept(Holder<? extends EffectHolder> enchantment, int level);
    }

    @FunctionalInterface
    public interface ContextAwareConsumer {
        void accept(Holder<? extends EffectHolder> enchantment, int level, EnchantedItemInUse context);
    }

}
