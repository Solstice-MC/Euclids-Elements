package org.solstice.euclidsElements.recipeBook.mixin.screen;

import net.minecraft.screen.AbstractRecipeScreenHandler;
import org.solstice.euclidsElements.recipeBook.api.EuclidsAbstractRecipeScreenHandler;
import org.solstice.euclidsElements.recipeBook.api.RecipeBookCategory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractRecipeScreenHandler.class)
public abstract class AbstractRecipeScreenHandlerMixin implements EuclidsAbstractRecipeScreenHandler {

	public abstract RecipeBookCategory getRecipeCategory();

}
