package org.solstice.euclidsElements.content.mixin.bundle;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.BundleItem;
import org.solstice.euclidsElements.content.api.item.bundle.EuclidsBundleContentsComponentBuilder;
import org.solstice.euclidsElements.content.api.item.bundle.EuclidsBundleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(BundleItem.class)
public class BundleItemMixin implements EuclidsBundleItem {

	@ModifyConstant(method = "appendTooltip", constant = @Constant(intValue = 64))
	private int changeCapacity(int v) {
		return this.getCapacity();
	}

	@WrapOperation(
		method = {"onStackClicked", "onClicked"},
		at = @At(
			value = "NEW",
			target = "(Lnet/minecraft/component/type/BundleContentsComponent;)Lnet/minecraft/component/type/BundleContentsComponent$Builder;"
		)
	)
	@SuppressWarnings("RedundantCast")
	public BundleContentsComponent.Builder setBuilderBundle(BundleContentsComponent base, Operation<BundleContentsComponent.Builder> original) {
		BundleContentsComponent.Builder builder = original.call(base);
		((EuclidsBundleContentsComponentBuilder)builder).setBundle((BundleItem)(Object)this);
		return builder;
	}

}
