package org.solstice.euclidsElements.recipeBook.mixin;

import net.minecraft.recipe.book.RecipeBook;
import net.minecraft.recipe.book.RecipeBookOptions;
import org.solstice.euclidsElements.recipeBook.api.EuclidsRecipeBook;
import org.solstice.euclidsElements.recipeBook.api.RecipeBookCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RecipeBook.class)
public class RecipeBookMixin implements EuclidsRecipeBook {

	@Shadow @Final private final RecipeBookOptions options = new RecipeBookOptions();

	@Override
	public boolean isGuiOpen(RecipeBookCategory category) {
		return this.options.isGuiOpen(category);
	}

	@Override
	public void setGuiOpen(RecipeBookCategory category, boolean open) {
		this.options.setGuiOpen(category, open);
	}

	@Override
	public boolean isFilteringCraftable(RecipeBookCategory category) {
		return this.options.isFilteringCraftable(category);
	}

	@Override
	public void setFilteringCraftable(RecipeBookCategory category, boolean filteringCraftable) {
		this.options.setFilteringCraftable(category, filteringCraftable);
	}

	@Override
	public void setCategoryOptions(RecipeBookCategory category, boolean guiOpen, boolean filteringCraftable) {
		this.options.setGuiOpen(category, guiOpen);
		this.options.setFilteringCraftable(category, filteringCraftable);
	}

}
