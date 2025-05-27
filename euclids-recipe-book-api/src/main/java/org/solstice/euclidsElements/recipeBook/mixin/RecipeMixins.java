package org.solstice.euclidsElements.recipeBook.mixin;

import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.RecipeBook;
import org.spongepowered.asm.mixin.Mixin;

public class RecipeMixins {

	@Mixin(ShapedRecipe.class)
	public static class ShapedRecipeMixin {

	}

	@Mixin(ShapedRecipe.Serializer.class)
	public static class ShapedRecipeSerializerMixin {

	}

}
