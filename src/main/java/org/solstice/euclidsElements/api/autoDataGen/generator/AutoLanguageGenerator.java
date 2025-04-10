package org.solstice.euclidsElements.api.autoDataGen.generator;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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


	public AutoLanguageGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public ModContainer getContainer() {
		return this.dataOutput.getModContainer();
	}


	@Override
	public void generateTranslations(RegistryWrapper.WrapperLookup lookup, TranslationBuilder builder) {
		generateRegistryTranslations(lookup, builder);
	}

	public void generateRegistryTranslations(RegistryWrapper.WrapperLookup lookup, TranslationBuilder builder) {
		lookup.streamAllRegistryKeys()
			.filter(AutoLanguageGenerator::keyIsNotBlacklisted)
			.map(lookup::getOptionalWrapper)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.forEach(wrapper -> wrapper.streamEntries()
				.filter(this::ownsEntry)
				.forEach(entry -> this.generateEntryTranslation(entry, builder))
			);
	}

	public void generateEntryTranslation(RegistryEntry<?> entry, TranslationBuilder builder) {
		RegistryKey<?> registryKey = entry.getKey().orElse(null);
		if (registryKey == null) return;

		Identifier id = registryKey.getValue();
		String dataType = registryKey.getRegistry().getPath();
		if (dataType.endsWith("_type")) dataType = dataType.split("_type")[0];
		String key = id.toTranslationKey(dataType);
		String translation = Arrays
			.stream(id.getPath().split("([_.])"))
			.map(AutoLanguageGenerator::toUpperCase)
			.collect(Collectors.joining(" "));
		builder.add(key, translation);
	}

//	public void generateTagTranslations(RegistryWrapper.WrapperLookup lookup) {
//		lookup.streamAllRegistryKeys().forEach(sKey ->
//			lookup.getWrapperOrThrow(sKey).streamTagKeys().forEach(this::generateTagTranslation)
//		);
//	}
//
//	public void generateTagTranslation(TagKey<?> tag) {
//		Identifier id = tag.id();
//		String key = id.toTranslationKey("tag." + tag.registry().getValue().getPath());
//		String translation = Arrays
//			.stream(id.getPath().split("([_.])"))
//			.map(AutoLanguageGenerator::toUpperCase)
//			.collect(Collectors.joining(" "));
//		this.add(key, translation);
//	}

}
