package org.solstice.euclidsElements.api.autoDataGen.provider;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.Tags;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class EuclidsLanguageProvider implements DataProvider {

	@Override
	public String getName() {
		return "Translations for mod: " + this.getModId();
	}

	protected final DataOutput output;
	protected final CompletableFuture<RegistryWrapper.WrapperLookup> lookupFuture;
	protected final ModContainer container;

	protected final Map<String, String> data = new TreeMap<>();

	public EuclidsLanguageProvider(ModContainer container, DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lookup) {
		this.output = output;
		this.lookupFuture = lookup;
		this.container = container;
	}

	public String getModId() {
		return this.container.getModId() + "-datagen";
	}

	protected abstract void addTranslations(RegistryWrapper.WrapperLookup lookup);

	@Override
	public CompletableFuture<?> run(DataWriter writer) {
		return this.lookupFuture.thenCompose((registryLookup) -> this.run(writer, registryLookup));
	}

	protected CompletableFuture<?> run(DataWriter writer, RegistryWrapper.WrapperLookup lookup) {
		this.addTranslations(lookup);

		if (!data.isEmpty())
			return save(writer, this.output.resolvePath(DataOutput.OutputType.RESOURCE_PACK).resolve(this.getModId()).resolve("lang").resolve("en_us.json"));

		return CompletableFuture.allOf();
	}

	protected CompletableFuture<?> save(DataWriter cache, Path target) {
		// TODO: DataProvider.saveStable handles the caching and hashing already, but creating the JSON Object this way seems unreliable. -C
		JsonObject json = new JsonObject();
		this.data.forEach(json::addProperty);

		return DataProvider.writeToPath(cache, json, target);
	}

	public void addBlock(Supplier<? extends Block> key, String name) {
		add(key.get(), name);
	}

	public void add(Block key, String name) {
		add(key.getTranslationKey(), name);
	}

	public void addItem(Supplier<? extends Item> key, String name) {
		add(key.get(), name);
	}

	public void add(Item key, String name) {
		add(key.getTranslationKey(), name);
	}

	public void addItemStack(Supplier<ItemStack> key, String name) {
		add(key.get(), name);
	}

	public void add(ItemStack key, String name) {
		add(key.getTranslationKey(), name);
	}

	public void addEffect(Supplier<? extends StatusEffect> key, String name) {
		add(key.get(), name);
	}

	public void add(StatusEffect key, String name) {
		add(key.getTranslationKey(), name);
	}

	public void addEntityType(Supplier<? extends EntityType<?>> key, String name) {
		add(key.get(), name);
	}

	public void add(EntityType<?> key, String name) {
		add(key.getTranslationKey(), name);
	}

	public void addTag(Supplier<? extends TagKey<?>> key, String name) {
		add(key.get(), name);
	}

	public void add(TagKey<?> tagKey, String name) {
		add(Tags.getTagTranslationKey(tagKey), name);
	}

	public void add(String key, String value) {
		if (data.put(key, value) != null)
			throw new IllegalStateException("Duplicate translation key " + key);
	}

}
