package org.solstice.euclidsElements.autoDatagen.api.generator;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.concurrent.CompletableFuture;

public class AutoLootTableGenerator extends FabricBlockLootTableProvider implements AutoGenerator {

	protected final FabricDataOutput dataOutput;

	public AutoLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, registryLookup);
		this.dataOutput = dataOutput;
	}

	@Override
	public String getModId() {
		return this.dataOutput.getModContainer().getMetadata().getId();
	}

	@Override
	public void generate() {
		this.registryLookup.getWrapperOrThrow(RegistryKeys.BLOCK)
			.streamEntries()
			.filter(this::ownsEntry)
			.map(RegistryEntry.Reference::value)
			.forEach(this::addDrop);
	}

}
