package org.solstice.euclidsElements.api.autoDataGen.generator;

import net.minecraft.data.DataOutput;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.neoforged.fml.ModContainer;
import org.solstice.euclidsElements.api.autoDataGen.provider.EuclidsItemModelProvider;

import java.util.concurrent.CompletableFuture;

public class AutoItemModelGenerator extends EuclidsItemModelProvider implements AutoGenerator {

	public AutoItemModelGenerator(ModContainer container, DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lookup) {
		super(container, output, lookup);
	}

	@Override
	public String getModId() {
		return this.container.getModId();
	}

	@Override
	protected void registerModels(RegistryWrapper.WrapperLookup lookup) {
		lookup.getWrapperOrThrow(RegistryKeys.ITEM)
			.streamEntries()
			.filter(this::ownsEntry)
			.forEach(this::registerModel);

		this.register(Identifier.of("test"), Models.CARPET);
	}

	protected void registerModel(RegistryEntry<Item> entry) {
		Identifier id = entry.getKey().orElseThrow().getValue();
		Item item = entry.value();
		this.register(id, Models.GENERATED);
	}

}
