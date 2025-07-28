package org.solstice.euclidsElements.content.test;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.function.Function;
import java.util.function.Supplier;

public class ConstructedRegistryContainer<T, R> extends RegistryContainer<T> {

	protected final Supplier<R> settings;
	protected final Function<R, T> constructor;

	public ConstructedRegistryContainer(String modId, Registry<T> registry, Supplier<R> defaultSettings, Function<R, T> defaultConstructor) {
		super(modId, registry);
		this.settings = defaultSettings;
		this.constructor = defaultConstructor;
	}

	public T register(String name) {
		return this.register(name, this.constructor);
	}

	public T register(String name, Function<R, T> function) {
		return this.register(name, function, this.settings.get());
	}

//	public T register(String name, R settings) {
//		return this.register(name, this.defaultConstructor, settings);
//	}

	public T register(String name, Function<R, T> constructor, R settings) {
		Identifier id = Identifier.of(this.modId, name);
		RegistryKey<T> key = RegistryKey.of(this.registry.getKey(), id);
		T value = constructor.apply(settings);
		return Registry.register(this.registry, key, value);
	}

}
