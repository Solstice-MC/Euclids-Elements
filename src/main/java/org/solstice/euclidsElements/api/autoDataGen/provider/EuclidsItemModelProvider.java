package org.solstice.euclidsElements.api.autoDataGen.provider;

import com.google.gson.JsonElement;
import net.minecraft.data.DataOutput;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class EuclidsItemModelProvider extends EuclidsModelProvider<ItemModelBuilder> {

	@Override
	public String getName() {
		return "Euclid's Item Model Provider";
	}

	@Override
	protected String getDirectory() {
		return "item";
	}

	public final BiConsumer<Identifier, Supplier<JsonElement>> writer;

	public EuclidsItemModelProvider(ModContainer container, DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lookup) {
		super(container, output, lookup);
		this.writer = (identifier, jsonElementSupplier) -> {};
	}

	@Override
	protected void registerModels(RegistryWrapper.WrapperLookup lookup) {
		this.register(Identifier.of("test"), Models.CARPET);
	}

	public void register(Item item, String suffix, Model model) {
		model.upload(ModelIds.getItemSubModelId(item, suffix), TextureMap.layer0(TextureMap.getSubId(item, suffix)), this.writer);
	}

	public void register(Identifier id, Model model) {
		model.upload(id, TextureMap.layer0(id), this.writer);
	}

}
