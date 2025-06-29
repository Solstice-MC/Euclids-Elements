package org.solstice.euclidsElements.recipeBook.mixin.screen;

import net.minecraft.screen.BlastFurnaceScreenHandler;
import org.solstice.euclidsElements.recipeBook.api.EuclidsAbstractRecipeScreenHandler;
import org.solstice.euclidsElements.recipeBook.api.RecipeBookCategory;
import org.solstice.euclidsElements.recipeBook.registry.VanillaRecipeBookCategories;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlastFurnaceScreenHandler.class)
public class BlastFurnaceScreenHandlerMixin implements EuclidsAbstractRecipeScreenHandler {

	@Override
	public RecipeBookCategory getRecipeCategory() {
		return VanillaRecipeBookCategories.BLAST_FURNACE;
	}

}
