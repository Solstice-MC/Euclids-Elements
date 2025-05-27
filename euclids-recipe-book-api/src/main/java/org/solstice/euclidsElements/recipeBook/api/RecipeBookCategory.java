package org.solstice.euclidsElements.recipeBook.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.EuclidsElements;

import java.util.stream.Stream;

public record RecipeBookCategory(Identifier id) {

	public static final RegistryKey<Registry<RecipeBookCategory>> REGISTRY_KEY = RegistryKey.ofRegistry(EuclidsElements.of("recipe_book_category"));
	public static final Registry<RecipeBookCategory> REGISTRY = new SimpleRegistry<>(REGISTRY_KEY, Lifecycle.stable());

	public static Codec<RecipeBookCategory> CODEC = REGISTRY.getCodec();

	public static Stream<RecipeBookCategory> streamCategories() {
		return REGISTRY.stream();
	}

}
