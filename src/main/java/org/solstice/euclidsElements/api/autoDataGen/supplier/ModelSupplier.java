package org.solstice.euclidsElements.api.autoDataGen.supplier;

import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModelSupplier<T, G, S extends ModelSupplier.ModelProvider<T, G>> {

	protected final Map<Class<? extends T>, S> modelProviders;

	public ModelSupplier() {
		this.modelProviders = new LinkedHashMap<>();
	}

	@FunctionalInterface
	public interface ModelProvider<T, G> {
		void generate(G generate, T type, ResourceLocation id);
	}

}
