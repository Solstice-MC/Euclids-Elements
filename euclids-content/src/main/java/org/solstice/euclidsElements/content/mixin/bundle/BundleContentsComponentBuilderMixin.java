package org.solstice.euclidsElements.content.mixin.bundle;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;
import org.solstice.euclidsElements.content.api.item.bundle.EuclidsBundleContentsComponentBuilder;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BundleContentsComponent.Builder.class)
public abstract class BundleContentsComponentBuilderMixin implements EuclidsBundleContentsComponentBuilder {

	@Unique public BundleItem bundle;

	@Override
	public void setBundle(BundleItem bundle) {
		this.bundle = bundle;
	}

	@WrapOperation(
		method = {"add*", "removeFirst", "getMaxAllowed"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/component/type/BundleContentsComponent;getOccupancy(Lnet/minecraft/item/ItemStack;)Lorg/apache/commons/lang3/math/Fraction;"
		)
	)
	public Fraction wrapGetOccupancy(ItemStack stack, Operation<Fraction> original) {
		if (this.bundle == null) return original.call(stack);
		return this.bundle.getStackOccupancy(stack);
	}

	@WrapMethod(method = "add(Lnet/minecraft/item/ItemStack;)I")
	public int wrapAdd(ItemStack stack, Operation<Integer> original) {
		if (this.bundle != null && !this.bundle.acceptsStack(stack)) return 0;
		return original.call(stack);
	}

}
