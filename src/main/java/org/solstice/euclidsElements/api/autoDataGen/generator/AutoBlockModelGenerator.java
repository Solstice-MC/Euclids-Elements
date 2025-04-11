package org.solstice.euclidsElements.api.autoDataGen.generator;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.solstice.euclidsElements.api.autoDataGen.supplier.BlockModelSupplier;

public class AutoBlockModelGenerator extends BlockStateProvider implements AutoGenerator {

	@Override
	public ModContainer getContainer() {
		return this.container;
	}

	protected final ModContainer container;

	public AutoBlockModelGenerator(ModContainer container, PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, container.getModId(), exFileHelper);
		this.container = container;
	}

	@Override
	protected void registerStatesAndModels() {
		BuiltInRegistries.BLOCK.holders()
			.filter(this::ownsEntry)
			.forEach(this::generateBlockModel);
	}

	public void generateBlockModel(Holder<Block> entry) {
		Block block = entry.value();
		ResourceLocation id = entry.getKey().location();
		BlockModelSupplier.generate(this, block, id);
	}

}
