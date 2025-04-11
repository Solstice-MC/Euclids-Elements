package org.solstice.euclidsElements.api.autoDataGen.generator;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModContainer;
import org.solstice.euclidsElements.api.autoDataGen.provider.EuclidsLanguageProvider;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AutoLanguageGenerator extends EuclidsLanguageProvider implements AutoGenerator {

	public static final List<ResourceKey<?>> REGISTRY_BLACKLIST = List.of(
		Registries.RECIPE_SERIALIZER,
		Registries.BIOME
	);

	public static boolean keyIsNotBlacklisted(ResourceKey<?> key) {
		return !REGISTRY_BLACKLIST.contains(key);
	}

	public static String toUpperCase(String str) {
		return str.substring(0,1).toUpperCase() + str.substring(1);
	}

	@Override
	public ModContainer getContainer() {
		return this.container;
	}

	public AutoLanguageGenerator(ModContainer container, PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
		super(container, output, lookup);
	}

	protected void addTranslations(HolderLookup.Provider lookup) {
		this.generateRegistryTranslations(lookup);
	}

	public void generateRegistryTranslations(HolderLookup.Provider lookup) {
		lookup.listRegistries()
			.filter(AutoLanguageGenerator::keyIsNotBlacklisted)
			.map(lookup::lookup)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.forEach(wrapper -> wrapper.listElements()
				.filter(this::ownsEntry)
				.forEach(this::generateEntryTranslation)
			);
	}

	public void generateEntryTranslation(Holder<?> entry) {
		ResourceKey<?> registryKey = entry.getKey();
		if (registryKey == null) return;

		ResourceLocation id = registryKey.location();
		String dataType = registryKey.registry().getPath();
		if (dataType.endsWith("_type")) dataType = dataType.split("_type")[0];
		String key = id.toLanguageKey(dataType);
		String translation = Arrays
			.stream(id.getPath().split("([_.])"))
			.map(AutoLanguageGenerator::toUpperCase)
			.collect(Collectors.joining(" "));
		this.add(key, translation);
	}

}
