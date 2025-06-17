package org.solstice.euclidsElements.tag.mixin;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.JsonOps;
import net.minecraft.registry.tag.TagGroupLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.tag.api.RemovalTagFile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(TagGroupLoader.class)
public class TagGroupLoaderMixin {

	@Inject(
		method = "loadTags",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/registry/tag/TagFile;replace()Z",
			shift = At.Shift.AFTER
		)
	)
	private void removeTags(
		ResourceManager resourceManager,
		CallbackInfoReturnable<Map<Identifier, List<TagGroupLoader.TrackedEntry>>> cir,
		@Local Resource resource,
		@Local JsonElement element,
		@Local List<TagGroupLoader.TrackedEntry> entries
	) {
		RemovalTagFile file = RemovalTagFile.CODEC.parse(JsonOps.INSTANCE, element).getOrThrow();

		file.removals().forEach(entry -> {
			entries.removeIf(loadedEntry -> loadedEntry.entry().id.equals(entry.id));
		});
	}

}
