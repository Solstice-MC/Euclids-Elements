package org.solstice.euclidsElements.recipeBook.mixin.screen;

import org.solstice.euclidsElements.recipeBook.api.EuclidsAbstractRecipeScreenHandler;
import org.solstice.euclidsElements.recipeBook.api.RecipeBookCategory;
import org.solstice.euclidsElements.recipeBook.registry.VanillaRecipeBookCategories;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SmokerScreenHandler.class)
public class SmokerScreenHandler implements EuclidsAbstractRecipeScreenHandler {

	@Override
	public RecipeBookCategory getRecipeCategory() {
		return VanillaRecipeBookCategories.SMOKER;
	}

}
