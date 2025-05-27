package org.solstice.euclidsElements.recipeBook.mixin.screen;

import net.minecraft.screen.CraftingScreenHandler;
import org.solstice.euclidsElements.recipeBook.api.EuclidsAbstractRecipeScreenHandler;
import org.solstice.euclidsElements.recipeBook.api.RecipeBookCategory;
import org.solstice.euclidsElements.recipeBook.registry.VanillaRecipeBookCategories;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin implements EuclidsAbstractRecipeScreenHandler {

	@Override
	public RecipeBookCategory getRecipeCategory() {
		return VanillaRecipeBookCategories.CRAFTING;
	}

}
