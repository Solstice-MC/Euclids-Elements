package org.solstice.euclidsElements.autoDatagen.api.supplier;

import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.Map;

public abstract class ModelSupplier<T, G, S extends ModelSupplier.ModelProvider<T, G>> {

	protected final LinkedList<Map.Entry<Class<? extends T>, S>> modelProviders;

	public ModelSupplier() {
		this.modelProviders = new LinkedList<>();
	}

	@FunctionalInterface
	public interface ModelProvider<T, G> {
		void generate(G generate, T type, Identifier id);
	}

}
