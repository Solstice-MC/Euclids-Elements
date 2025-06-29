package org.solstice.euclidsElements.advancement.api;

import net.minecraft.advancement.Advancement;

import java.util.List;
import java.util.function.UnaryOperator;

public interface EuclidsAdvancementBuilder {

	default Advancement.Builder requirements(List<String> requirement) {
		return null;
	}

	default Advancement.Builder requirements(String... requirement) {
		return null;
	}

	default Advancement.Builder modifyRequirements(UnaryOperator<List<List<String>>> function) {
		return null;
	}

	default Advancement build() {
		return null;
	}

}
