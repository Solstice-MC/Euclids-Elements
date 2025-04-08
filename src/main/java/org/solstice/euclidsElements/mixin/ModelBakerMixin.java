package org.solstice.euclidsElements.mixin;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class ModelBakerMixin {

	@Shadow protected abstract void loadItemModelAndDependencies(ResourceLocation id);

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelBakery;loadItemModelAndDependencies(Lnet/minecraft/resources/ResourceLocation;)V"))
	private void ignored(ModelBakery instance, ResourceLocation id) {}

	@Inject(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V",
			ordinal = 0,
			shift = At.Shift.AFTER
		)
	)
	private void loadAllItemModels (
		BlockColors blockColors,
		ProfilerFiller profiler,
		Map<ResourceLocation, BlockModel> jsonUnbakedModels,
		Map<ResourceLocation, List<BlockStateModelLoader.LoadedJson>> blockStates,
		CallbackInfo ci
	) {
		jsonUnbakedModels.keySet().stream()
			.filter(id -> id.getPath().startsWith("models/item"))
			.map(id -> id.withPath(path -> path.replace("models/item/", "").replace(".json", "")))
			.forEach(this::loadItemModelAndDependencies);
	}

}
