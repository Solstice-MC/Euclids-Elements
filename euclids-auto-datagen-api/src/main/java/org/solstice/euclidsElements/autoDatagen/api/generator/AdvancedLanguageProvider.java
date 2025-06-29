package org.solstice.euclidsElements.autoDatagen.api.generator;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class AdvancedLanguageProvider extends FabricLanguageProvider {

	private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup;

	public AdvancedLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, registryLookup);
		this.registryLookup = registryLookup;
	}

	@Override
	public CompletableFuture<?> run(DataWriter writer) {
		TreeMap<String, String> translationEntries = new TreeMap<>();
		return this.registryLookup.thenCompose(lookup -> {
			this.generateTranslations(lookup, (key, value) -> {
				Objects.requireNonNull(key);
				Objects.requireNonNull(value);
				if (translationEntries.containsKey(key)) {
					throw new RuntimeException("Existing translation key found - " + key + " - Duplicate will be ignored.");
				} else {
					translationEntries.put(key, value);
				}
			});
			JsonObject object = new JsonObject();
			translationEntries.forEach(object::addProperty);
			return DataProvider.writeToPath(writer, object, this.getLangFilePath());
		});
	}

	protected Path getLangFilePath() {
		String modId = this.dataOutput.getModId();
		if (!modId.endsWith("datagen")) modId += "-datagen";
		Identifier path = Identifier.of(modId, "en_us");
		return this.dataOutput.getResolver(DataOutput.OutputType.RESOURCE_PACK, "lang").resolveJson(path);
	}

}
