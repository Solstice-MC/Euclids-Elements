package org.solstice.euclidsElements.content.mixin.customItemModel;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.content.registry.EuclidsComponentTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

	@Shadow
	@Final
	private ItemModels models;

	@Shadow
	protected abstract void renderBakedItemModel(BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices);

	@Shadow
	public static VertexConsumer getDirectItemGlintConsumer(VertexConsumerProvider provider, RenderLayer layer, boolean solid, boolean glint) {
		return null;
	}

	@Inject(method = "renderItem", at = @At(value = "HEAD"), cancellable = true)
	public void renderItem(
		ItemStack stack,
		ModelTransformationMode renderMode,
		boolean leftHanded,
		MatrixStack matrices,
		VertexConsumerProvider vertexConsumers,
		int light,
		int overlay,
		BakedModel model,
		CallbackInfo ci
	) {
		if (stack.isEmpty()) return;

		boolean shouldRenderIcon = renderMode == ModelTransformationMode.GUI
			|| renderMode == ModelTransformationMode.GROUND
			|| renderMode == ModelTransformationMode.FIXED;
		if (!shouldRenderIcon) return;

		Identifier id = stack.getOrDefault(EuclidsComponentTypes.INVENTORY_ITEM_MODEL, null);
		if (id == null) return;

		ModelIdentifier modelId = ModelIdentifier.ofInventoryVariant(id);
//		ClientWorld clientWorld = world instanceof ClientWorld ? (ClientWorld) world : null;
		matrices.push();
		model = this.models.getModelManager().getModel(modelId);
		model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
		matrices.translate(-0.5F, -0.5F, -0.5F);
//		bakedModel = bakedModel.getOverrides().apply(bakedModel, stack, clientWorld, entity, seed);
//		cir.setReturnValue(bakedModel == null ? this.models.getModelManager().getMissingModel() : bakedModel);
		RenderLayer renderLayer = RenderLayers.getItemLayer(stack, true);

		VertexConsumer vertexConsumer = getDirectItemGlintConsumer(vertexConsumers, renderLayer, true, stack.hasGlint());
		this.renderBakedItemModel(model, stack, light, overlay, matrices, vertexConsumer);
		matrices.pop();
		ci.cancel();
	}

//	@Inject(method = "getModel", at = @At(value = "HEAD"), cancellable = true)
//	public void getModel(ItemStack stack, World world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
//		Identifier id = stack.getOrDefault(EuclidsComponentTypes.INVENTORY_ITEM_MODEL, null);
//		if (id == null) return;
//
//		ModelIdentifier modelId = ModelIdentifier.ofInventoryVariant(id);
//		ClientWorld clientWorld = world instanceof ClientWorld ? (ClientWorld) world : null;
//		BakedModel bakedModel = this.models.getModelManager().getModel(modelId);
//		bakedModel = bakedModel.getOverrides().apply(bakedModel, stack, clientWorld, entity, seed);
//		cir.setReturnValue(bakedModel == null ? this.models.getModelManager().getMissingModel() : bakedModel);
//	}

}
