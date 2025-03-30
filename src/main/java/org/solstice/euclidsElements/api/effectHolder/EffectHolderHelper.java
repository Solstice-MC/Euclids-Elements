package org.solstice.euclidsElements.api.effectHolder;

import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
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

    public static final List<ComponentType<? extends EffectHolderComponent<?>>> EFFECT_HOLDER_COMPONENTS = new ArrayList<>();

	public static final TagKey<ComponentType<?>> EFFECT_HOLDER =
		TagKey.of(RegistryKeys.DATA_COMPONENT_TYPE, EuclidsElements.of("effect_holder"));

	@SubscribeEvent
	public static void onAddReloadListeners(AddReloadListenerEvent event) {
		initializeEffectHolderComponents(event.getRegistryAccess());
	}

	@SubscribeEvent
	public static void onServerAboutToStart(ServerAboutToStartEvent event) {
		initializeEffectHolderComponents(event.getServer().getRegistryManager().toImmutable());
	}

	@SuppressWarnings("unchecked")
	public static void initializeEffectHolderComponents(RegistryWrapper.WrapperLookup lookup) {
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

            EnchantmentEffectContext context = new EnchantmentEffectContext(stack, slot, entity);
            component.getEffects().forEach((entry, level) -> {
                if (entry.value().slotMatches(slot)) {
                    contextAwareConsumer.accept(entry, level, context);
                }
            });
        });
    }

    public static void forEachEffectHolder(LivingEntity entity, ContextAwareConsumer consumer) {
        for(EquipmentSlot slot : EquipmentSlot.values()) {
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
    public interface Consumer {
        void accept(RegistryEntry<? extends EffectHolder> enchantment, int level);
    }

    @FunctionalInterface
    public interface ContextAwareConsumer {
        void accept(RegistryEntry<? extends EffectHolder> enchantment, int level, EnchantmentEffectContext context);
    }

}
