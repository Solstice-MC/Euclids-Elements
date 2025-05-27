package org.solstice.euclidsElements.recipeBook.mixin.screen;

import org.solstice.euclidsElements.recipeBook.api.EuclidsAbstractRecipeScreenHandler;
import org.solstice.euclidsElements.recipeBook.api.RecipeBookCategory;
import org.solstice.euclidsElements.recipeBook.registry.VanillaRecipeBookCategories;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FurnaceScreenHandler.class)
public class FurnaceScreenHandler implements EuclidsAbstractRecipeScreenHandler {

	@Override
	public RecipeBookCategory getRecipeCategory() {
		return VanillaRecipeBookCategories.FURNACE;
	}

}
