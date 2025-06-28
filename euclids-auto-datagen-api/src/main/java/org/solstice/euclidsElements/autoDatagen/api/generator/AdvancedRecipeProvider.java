package org.solstice.euclidsElements.autoDatagen.api.generator;

import com.google.common.collect.Sets;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class AdvancedRecipeProvider extends RecipeProvider {

	protected final DataOutput.PathResolver recipesPathResolver;
	protected final DataOutput.PathResolver advancementsPathResolver;

	public AdvancedRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(output, registryLookup);
		this.recipesPathResolver = output.getResolver(RegistryKeys.RECIPE);
		this.advancementsPathResolver = output.getResolver(RegistryKeys.ADVANCEMENT);
	}

	@Override
	public CompletableFuture<?> run(DataWriter writer, RegistryWrapper.WrapperLookup registryLookup) {
		final Set<Identifier> recipes = Sets.newHashSet();
		final List<CompletableFuture<?>> result = new ArrayList<>();

		RecipeExporter exporter = new RecipeExporter() {

			public void accept(Identifier id, Recipe<?> recipe, @Nullable AdvancementEntry advancement) {
				if (!recipes.add(id)) {
					throw new IllegalStateException("Duplicate recipe " + id);
				}
				result.add(DataProvider.writeCodecToPath(writer, registryLookup, Recipe.CODEC, recipe, AdvancedRecipeProvider.this.recipesPathResolver.resolveJson(id)));
				if (advancement != null)
					result.add(DataProvider.writeCodecToPath(writer, registryLookup, Advancement.CODEC, advancement.value(), AdvancedRecipeProvider.this.advancementsPathResolver.resolveJson(advancement.id())));
			}

			public Advancement.Builder getAdvancementBuilder() {
				AdvancementEntry entry = new AdvancementEntry(CraftingRecipeJsonBuilder.ROOT, null);
				return Advancement.Builder.createUntelemetered().parent(entry);
			}

		};

		this.generate(exporter, registryLookup);
		return CompletableFuture.allOf(result.toArray(CompletableFuture[]::new));
	}

	@Override public void generate(RecipeExporter exporter) {}

	public abstract void generate(RecipeExporter exporter, RegistryWrapper.WrapperLookup registryLookup);

}
