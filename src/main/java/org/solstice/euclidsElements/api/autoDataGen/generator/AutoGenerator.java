package org.solstice.euclidsElements.api.autoDataGen.generator;

import net.fabricmc.loader.api.ModContainer;
import net.minecraft.registry.entry.RegistryEntry;

public interface AutoGenerator {

	ModContainer getContainer();

	default String getId() {
		return this.getContainer().getMetadata().getId();
	}

	default boolean ownsEntry(RegistryEntry<?> entry) {
		return entry.getKey().orElseThrow().getValue().getNamespace().equals(this.getId());
	}

}
