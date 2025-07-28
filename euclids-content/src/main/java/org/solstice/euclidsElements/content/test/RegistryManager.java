package org.solstice.euclidsElements.content.test;

import net.minecraft.block.Block;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.awt.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegistryManager {

	protected final String modId;

	protected RegistryManager(String modId) {
		this.modId = modId;
	}

	public static RegistryManager of(String modId) {
		return new RegistryManager(modId);
	}

	public ConstructedRegistryContainer<Item, Item.Settings> item() {
		return this.constructedRegistry(Registries.ITEM, Item.Settings::new, Item::new);
	}

	public ConstructedRegistryContainer<Block, Block.Settings> block() {
		return this.constructedRegistry(Registries.BLOCK, Block.Settings::create, Block::new);
	}

	public ComponentRegistryContainer component() {
		return this.component(Registries.DATA_COMPONENT_TYPE);
	}

	public ComponentRegistryContainer component(Registry<ComponentType<?>> registry) {
		return new ComponentRegistryContainer(this.modId, registry);
	}

	public <T> RegistryContainer<T> registry(Registry<T> registry) {
		return new RegistryContainer<>(this.modId, registry);
	}

	public <T, R> ConstructedRegistryContainer<T, R> constructedRegistry(Registry<T> registry, Supplier<R> settings, Function<R, T> constructor) {
		return new ConstructedRegistryContainer<>(this.modId, registry, settings, constructor);
	}

}
