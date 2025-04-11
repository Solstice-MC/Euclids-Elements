package org.solstice.euclidsElements.api.autoDataGen.generator;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.solstice.euclidsElements.api.autoDataGen.supplier.ItemModelSupplier;

public class AutoItemModelGenerator extends ItemModelProvider implements AutoGenerator {

	@Override
	public ModContainer getContainer() {
		return this.container;
	}

	protected final ModContainer container;

	public AutoItemModelGenerator(ModContainer container, PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, container.getModId(), exFileHelper);
		this.container = container;
	}

	@Override
	protected void registerModels() {
		BuiltInRegistries.ITEM.holders()
			.filter(this::ownsEntry)
			.forEach(this::generateItemModel);
	}

	public void generateItemModel(Holder<Item> entry) {
		Item item = entry.value();
		ResourceLocation id = entry.getKey().location();
		ItemModelSupplier.generate(this, item, id);
	}

}
