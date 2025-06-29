package org.solstice.euclidsElements.autoDatagen.api.generator;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.autoDatagen.api.supplier.BlockModelSupplier;
import org.solstice.euclidsElements.autoDatagen.api.supplier.ItemModelSupplier;

public class AutoModelGenerator extends FabricModelProvider implements AutoGenerator {

	private final FabricDataOutput output;

	public AutoModelGenerator(FabricDataOutput output) {
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

	@Override
	public void generateItemModels(ItemModelGenerator generator) {
		Registries.ITEM.streamEntries()
			.filter(this::ownsEntry)
			.forEach(entry -> this.generateItemModel(entry, generator));
	}

	public void generateItemModel(RegistryEntry<Item> entry, ItemModelGenerator generator) {
		Item item = entry.value();
		Identifier id = entry.getKeyOrValue().left().orElseThrow().getValue();
		ItemModelSupplier.generate(generator, item, id);
	}

}
