package org.solstice.euclidsElements.advancement.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.advancement.api.AdvancementEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerAdvancementLoader.class)
public class AdvancementLoaderMixin {

	@Shadow @Final private RegistryWrapper.WrapperLookup registryLookup;

	@ModifyVariable(method = "method_20723", at = @At("STORE"), ordinal = 0)
	private Advancement injected(Advancement advancement, @Local(argsOnly = true) Identifier id) {
		Advancement.Builder builder = fromAdvancement(advancement);

		RegistryKey<Advancement> key = RegistryKey.of(RegistryKeys.ADVANCEMENT, id);
		AdvancementEvents.MODIFY.invoker().modifyLootTable(key, builder, advancement, this.registryLookup);
		return builder.build();
	}

	@Unique
	private static Advancement.Builder fromAdvancement(Advancement advancement) {
		Advancement.Builder builder = Advancement.Builder.create();
		if (advancement.parent().isPresent()) builder.parent(new AdvancementEntry(advancement.parent().get(), advancement));
		if (advancement.display().isPresent()) builder.display(advancement.display().get());
		builder.rewards(advancement.rewards());
		advancement.criteria().forEach((builder::criterion));
		builder.requirements(advancement.requirements());
		return builder;
	}

}
