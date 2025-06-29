package org.solstice.euclidsElements.effectHolder.mixin;

import net.minecraft.component.ComponentHolder;
import net.minecraft.item.ItemStack;
import org.solstice.euclidsElements.effectHolder.api.EffectHolderHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder {

	@Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
	private void injected(CallbackInfoReturnable<Integer> cir) {
		int result = cir.getReturnValue();
		result = EffectHolderHelper.getMaxDurability((ItemStack)(Object)this, result);
		cir.setReturnValue(result);
	}

}
