package org.solstice.euclidsElements.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.solstice.euclidsElements.registry.EuclidsComponents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemModelShaper.class)
public abstract class ItemModelsMixin {

	@Shadow public abstract BakedModel getItemModel(Item item);

	@Shadow @Final private ModelManager modelManager;

	@Redirect(
		method = "getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/ItemModelShaper;getItemModel(Lnet/minecraft/world/item/Item;)Lnet/minecraft/client/resources/model/BakedModel;"
		)
	)
	private BakedModel getCustomModel(ItemModelShaper instance, Item item, @Local(argsOnly = true) ItemStack stack) {
		ResourceLocation customModelId = stack.getOrDefault(EuclidsComponents.CUSTOM_ITEM_MODEL, null);
		if (customModelId == null) return this.getItemModel(item);
		return this.modelManager.getModel(ModelResourceLocation.inventory(customModelId));
	}

}
