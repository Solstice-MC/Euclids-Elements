package org.solstice.euclidsElements.enchantmentTooltips.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import org.solstice.euclidsElements.effectHolder.api.component.EffectHolderComponent;
import org.solstice.euclidsElements.enchantmentTooltips.api.EuclidsEnchantmentEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(ItemEnchantmentsComponent.class)
public abstract class ItemEnchantmentsComponentMixin implements EffectHolderComponent<Enchantment> {

	@Shadow @Final Object2IntOpenHashMap<RegistryEntry<Enchantment>> enchantments;

	@ModifyVariable(method = "appendTooltip", at = @At("STORE"))
	private RegistryEntry<Enchantment> getEntryReference(RegistryEntry<Enchantment> entry, @Share("entry") LocalRef<RegistryEntry<Enchantment>> reference) {
		reference.set(entry);
		return entry;
	}

	@Inject(
		method = "appendTooltip",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V",
			shift = At.Shift.AFTER
		)
	)
	private void addEffectDescriptions(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type, CallbackInfo ci, @Share("entry") LocalRef<RegistryEntry<Enchantment>> reference) {
		RegistryEntry<Enchantment> entry = reference.get();
		Enchantment enchantment = entry.value();
		ComponentMap effects = enchantment.getEffects();
		int level = this.enchantments.getInt(entry);
		boolean advanced = type == TooltipType.ADVANCED;

		effects.stream()
			.map(component -> EuclidsEnchantmentEffect.getDescription(effects, component, level))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.forEach(tooltip);
	}

}
