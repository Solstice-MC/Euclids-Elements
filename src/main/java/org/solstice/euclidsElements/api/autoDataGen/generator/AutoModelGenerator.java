package org.solstice.euclidsElements.api.autoDataGen.generator;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.api.autoDataGen.supplier.BlockModelSupplier;

public class AutoModelGenerator extends FabricModelProvider implements AutoGenerator {

	private final FabricDataOutput output;

	public AutoModelGenerator(FabricDataOutput output) {
		super(output);
		this.output = output;
	}

	@Override
	public ModContainer getContainer() {
		return this.output.getModContainer();
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator generator) {
		Registries.BLOCK.streamEntries()
			.filter(this::ownsEntry)
			.forEach(entry -> this.generateBlockModel(entry, generator));
	}

	public void generateBlockModel(RegistryEntry<Block> entry, BlockStateModelGenerator generator) {
		Block block = entry.value();
		Identifier id = entry.getKeyOrValue().left().orElseThrow().getValue();
		BlockModelSupplier.generate(generator, block, id);
	}

	@Override public void generateItemModels(ItemModelGenerator itemModelGenerator) {}

}
