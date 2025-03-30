package org.solstice.euclidsElements.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
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
import java.util.Objects;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder {

    @Shadow public abstract <T extends TooltipAppender> void appendTooltip(ComponentType<T> componentType, Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type);

	@Unique private static final TagKey<ComponentType<?>> TOOLTIP_HOLDER =
		TagKey.of(RegistryKeys.DATA_COMPONENT_TYPE, EuclidsElements.of("tooltip_holder"));

    @Inject(
            method = "getTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V",
                    ordinal = 0
            )
    )
	@SuppressWarnings("unchecked")
	private void addCustomComponentTooltips(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local Consumer<Text> consumer) {
		RegistryEntryList<ComponentType<?>> entries = RegistryHelper.getTagValues(context.getRegistryLookup(), RegistryKeys.DATA_COMPONENT_TYPE, TOOLTIP_HOLDER);
		entries.forEach(entry -> {
			ComponentType<?> component = entry.value();
			try {
				ComponentType<? extends TooltipAppender> appender = (ComponentType<? extends TooltipAppender>) component;
				this.appendTooltip(appender, context, consumer, type);
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
