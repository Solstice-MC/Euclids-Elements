package org.solstice.euclidsElements.api.autoDataGen;

import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class AutoItemModelGenerator extends ItemModelProvider {

	protected final CompletableFuture<RegistryWrapper.WrapperLookup> lookupFuture;
	protected final ModContainer container;

	public AutoItemModelGenerator(ModContainer container, DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lookup, ExistingFileHelper existingFileHelper) {
		super(output, container.getModId(), existingFileHelper);
		this.container = container;
		this.lookupFuture = lookup;
	}

	@Override
	protected void registerModels() {

	}

}
