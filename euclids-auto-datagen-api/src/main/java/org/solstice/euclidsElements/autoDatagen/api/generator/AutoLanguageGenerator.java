package org.solstice.euclidsElements.autoDatagen.api.generator;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AutoLanguageGenerator extends FabricLanguageProvider implements AutoGenerator {

	public static final List<RegistryKey<?>> REGISTRY_BLACKLIST = List.of(
		RegistryKeys.RECIPE_SERIALIZER,
		RegistryKeys.BIOME
	);

	public static boolean keyIsNotBlacklisted(RegistryKey<?> key) {
		return !REGISTRY_BLACKLIST.contains(key);
	}

	public static String toUpperCase(String str) {
		return str.substring(0,1).toUpperCase() + str.substring(1);
	}

	public static String translationFromIdentifier(Identifier id) {
		return Arrays
			.stream(id.getPath().split("([_.])"))
			.map(AutoLanguageGenerator::toUpperCase)
			.collect(Collectors.joining(" "));
	}

	private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup;

	public AutoLanguageGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, registryLookup);
		this.registryLookup = registryLookup;
	}

	@Override
	public String getModId() {
		return this.dataOutput.getModContainer().getMetadata().getId();
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
		return this.dataOutput.getResolver(DataOutput.OutputType.RESOURCE_PACK, "lang").resolveJson(Identifier.of(this.dataOutput.getModId() + "-datagen", "en_us"));
	}

	@Override
	public void generateTranslations(RegistryWrapper.WrapperLookup lookup, TranslationBuilder builder) {
		lookup.streamAllRegistryKeys()
			.filter(AutoLanguageGenerator::keyIsNotBlacklisted)
			.map(lookup::getOptionalWrapper)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.forEach(wrapper -> wrapper.streamEntries()
				.filter(this::ownsEntry)
				.forEach(entry -> this.generateEntryTranslation(entry, builder))
			);
//		lookup.streamAllRegistryKeys()
//			.map(lookup::getWrapperOrThrow)
//			.forEach(registry ->
//				registry.streamTagKeys().forEach(tag -> this.generateTagTranslation(tag, builder))
//			);
	}

	public void generateEntryTranslation(RegistryEntry<?> entry, TranslationBuilder builder) {
		RegistryKey<?> registryKey = entry.getKey().orElse(null);
		if (registryKey == null) return;

		Identifier id = registryKey.getValue();
		String dataType = registryKey.getRegistry().getPath();
		if (dataType.endsWith("_type")) dataType = dataType.split("_type")[0];
		String key = id.toTranslationKey(dataType);
		if (Language.getInstance().hasTranslation(key)) return;

		String translation = translationFromIdentifier(id);
		builder.add(key, translation);
	}

	public void generateTagTranslation(TagKey<?> tag, TranslationBuilder builder) {
		Identifier id = tag.id();
		String key = id.toTranslationKey("tag." + tag.registry().getValue().getPath());
		if (Language.getInstance().hasTranslation(key)) return;

		String translation = translationFromIdentifier(id);
		builder.add(key, translation);
	}

}
