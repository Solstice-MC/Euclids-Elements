package org.solstice.euclidsElements.content.test;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TagRegistryContainer<T> {

	protected final String modId;
	protected final Registry<T> registry;

	public TagRegistryContainer(String modId, Registry<T> registry) {
		this.modId = modId;
		this.registry = registry;
	}

	public T register(String name, T value) {
		Identifier id = Identifier.of(this.modId, name);
		return Registry.register(this.registry, id, value);
	}

}
