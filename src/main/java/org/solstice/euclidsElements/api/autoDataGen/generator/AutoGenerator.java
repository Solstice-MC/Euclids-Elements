package org.solstice.euclidsElements.api.autoDataGen.generator;

import net.minecraft.core.Holder;
import net.neoforged.fml.ModContainer;

public interface AutoGenerator {

	ModContainer getContainer();

	default String getModId() {
		return this.getContainer().getModId();
	}

	default boolean ownsEntry(Holder<?> entry) {
		return entry.getKey().location().getNamespace().equals(this.getModId());
	}

}
