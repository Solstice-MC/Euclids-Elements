package org.solstice.euclidsElements.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.api.effectHolder.EffectHolderHelper;
import org.solstice.euclidsElements.util.RegistryHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder {

    @Shadow public abstract <T extends TooltipProvider> void addToTooltip(DataComponentType<T> componentType, Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type);

	@Unique private static final TagKey<DataComponentType<?>> TOOLTIP_HOLDER =
		TagKey.create(Registries.DATA_COMPONENT_TYPE, EuclidsElements.of("tooltip_holder"));

    @Inject(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
                    ordinal = 0
            )
    )
	@SuppressWarnings("unchecked")
	private void addCustomComponentTooltips(Item.TooltipContext context, Player player, TooltipFlag type, CallbackInfoReturnable<List<Component>> cir, @Local Consumer<Component> consumer) {
		HolderSet<DataComponentType<?>> entries = RegistryHelper.getTagValues(context.registries(), Registries.DATA_COMPONENT_TYPE, TOOLTIP_HOLDER);
		entries.forEach(entry -> {
			DataComponentType<?> component = entry.value();
			try {
				DataComponentType<? extends TooltipProvider> appender = (DataComponentType<? extends TooltipProvider>) component;
				this.addToTooltip(appender, context, consumer, type);
			} catch (ClassCastException ignored) {}
		});
    }

	@Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
	private void injected(CallbackInfoReturnable<Integer> cir) {
		int result = cir.getReturnValue();
		result = EffectHolderHelper.getMaxDurability((ItemStack)(Object)this, result);
		cir.setReturnValue(result);
	}

}
