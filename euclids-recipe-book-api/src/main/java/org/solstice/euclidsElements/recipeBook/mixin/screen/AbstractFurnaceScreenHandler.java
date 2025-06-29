package org.solstice.euclidsElements.recipeBook.mixin.screen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AbstractFurnaceScreenHandler.class)
public class AbstractFurnaceScreenHandler {

	/**
	 * @author Solstice
	 * @reason Replace RecipeBookCategory
	 */
	@Overwrite
	public net.minecraft.recipe.book.RecipeBookCategory getCategory() {
		return net.minecraft.recipe.book.RecipeBookCategory.CRAFTING;
	}

}
