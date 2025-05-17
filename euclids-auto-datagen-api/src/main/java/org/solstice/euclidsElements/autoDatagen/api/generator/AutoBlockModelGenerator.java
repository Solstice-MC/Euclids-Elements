package org.solstice.euclidsElements.autoDatagen.api.generator;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.autoDatagen.api.supplier.BlockModelSupplier;

public class AutoBlockModelGenerator extends FabricModelProvider implements AutoGenerator {

	@Override
	public String getName() {
		return "Block Model Definitions";
	}

	private final FabricDataOutput output;

	public AutoBlockModelGenerator(FabricDataOutput output) {
		super(output);
		this.output = output;
	}

	@Override
	public String getModId() {
		return this.output.getModContainer().getMetadata().getId();
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
