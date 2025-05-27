package org.solstice.euclidsElements.recipeBook.registry;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.recipeBook.api.RecipeBookCategory;

import java.util.function.Function;

public class VanillaRecipeBookCategories {

	public static void init() {}

	public static final RecipeBookCategory CRAFTING = register("crafting", RecipeBookCategory::new);
	public static final RecipeBookCategory FURNACE = register("furnace", RecipeBookCategory::new);
	public static final RecipeBookCategory BLAST_FURNACE = register("blast_furnace", RecipeBookCategory::new);
	public static final RecipeBookCategory SMOKER = register("smoker", RecipeBookCategory::new);

	protected static RecipeBookCategory register(String name, Function<Identifier, RecipeBookCategory> function) {
		Identifier id = Identifier.of(name);
		return Registry.register(RecipeBookCategory.REGISTRY, id, function.apply(id));
	}

}
