package org.solstice.euclidsElements.api.autoDataGen.provider;

import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class EuclidsModelProvider<T extends ModelBuilder<T>> implements DataProvider {

	protected static final ExistingFileHelper.ResourceType MODEL = new ExistingFileHelper.ResourceType(net.minecraft.resource.ResourceType.CLIENT_RESOURCES, ".json", "models");

	protected final DataOutput output;
	protected final CompletableFuture<RegistryWrapper.WrapperLookup> lookupFuture;
	protected final ModContainer container;

	public final Map<Identifier, T> generatedModels = new HashMap<>();

	protected abstract void registerModels(RegistryWrapper.WrapperLookup lookup);
	protected abstract String getDirectory();

	public EuclidsModelProvider(ModContainer container, DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lookup) {
		this.output = output;
		this.lookupFuture = lookup;
		this.container = container;
	}

	@Override
	public CompletableFuture<?> run(DataWriter writer) {
		return this.lookupFuture.thenCompose((registryLookup) -> this.run(writer, registryLookup));
	}

	protected CompletableFuture<?> run(DataWriter writer, RegistryWrapper.WrapperLookup lookup) {
		this.registerModels(lookup);

		CompletableFuture<?>[] futures = new CompletableFuture<?>[this.generatedModels.size()];
		int i = 0;

		for (T model : this.generatedModels.values()) {
			Identifier location = model.getLocation();
			Path path = this.output.resolvePath(DataOutput.OutputType.RESOURCE_PACK)
				.resolve(location.getNamespace())
				.resolve("models")
				.resolve(location.getPath() + ".json");
			futures[i++] = DataProvider.writeToPath(writer, model.toJson(), path);
		}

		return CompletableFuture.allOf(futures);
	}

}
