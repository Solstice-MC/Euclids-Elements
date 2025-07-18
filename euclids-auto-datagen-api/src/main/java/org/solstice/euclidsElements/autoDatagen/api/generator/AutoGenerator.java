package org.solstice.euclidsElements.autoDatagen.api.generator;

import net.minecraft.registry.entry.RegistryEntry;

public interface AutoGenerator {

	String getModId();

	default boolean ownsEntry(RegistryEntry<?> entry) {
		return entry.getKey().orElseThrow().getValue().getNamespace().equals(this.getModId());
	}

}
