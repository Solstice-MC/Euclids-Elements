package org.solstice.tabula.registry;

import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.BiomeKeys;
import org.solstice.euclidsElements.advancement.api.AdvancementEvents;
import org.solstice.euclidsElements.advancement.api.EuclidsAdvancementBuilder;

public class TabulaAdvancements {

	public static void init() {
		AdvancementEvents.MODIFY.register((key, builder, original, registries) -> {
			if (key.getValue().toString().equals("minecraft:nether/explore_nether")) {
				builder.criterion("tabula:plains_for_some_reason", TickCriterion.Conditions.createLocation(LocationPredicate.Builder.createBiome(
					registries.getWrapperOrThrow(RegistryKeys.BIOME).getOrThrow(BiomeKeys.PLAINS)
				)));
				((EuclidsAdvancementBuilder)builder).requirements("tabula:plains_for_some_reason");
			}
		});
	}

}
