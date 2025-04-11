package org.solstice.euclidsElements.api.autoDataGen.provider;

import com.google.gson.JsonObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.fml.ModContainer;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public abstract class EuclidsLanguageProvider implements DataProvider {

	@Override
	public String getName() {
		return "Euclid's Translations for " + this.container.getModId();
	}

	protected final PackOutput output;
	protected final CompletableFuture<HolderLookup.Provider> lookupFuture;
	protected final ModContainer container;

	protected final Map<String, String> data = new TreeMap<>();

	public EuclidsLanguageProvider(ModContainer container, PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
		this.output = output;
		this.lookupFuture = lookup;
		this.container = container;
	}

	public String getModId() {
		return this.container.getModId() + "-datagen";
	}

	protected abstract void addTranslations(HolderLookup.Provider lookup);

	@Override
	public CompletableFuture<?> run(CachedOutput writer) {
		return this.lookupFuture.thenCompose((registryLookup) -> this.run(writer, registryLookup));
	}

	protected CompletableFuture<?> run(CachedOutput writer, HolderLookup.Provider lookup) {
		this.addTranslations(lookup);
		if (this.data.isEmpty()) return CompletableFuture.allOf();
		return save(writer, this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
			.resolve(this.getModId())
			.resolve("lang")
			.resolve("en_us.json")
		);
	}

	protected CompletableFuture<?> save(CachedOutput cache, Path target) {
		JsonObject json = new JsonObject();
		this.data.forEach(json::addProperty);
		return DataProvider.saveStable(cache, json, target);
	}


	public void add(String key, String value) {
		if (data.put(key, value) != null)
			throw new IllegalStateException("Duplicate translation key " + key);
	}

}
