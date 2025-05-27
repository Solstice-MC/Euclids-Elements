package org.solstice.euclidsElements.recipeBook.api;

public interface EuclidsRecipeBook {

	boolean isGuiOpen(RecipeBookCategory category);

	void setGuiOpen(RecipeBookCategory category, boolean open);

	boolean isFilteringCraftable(RecipeBookCategory category);

	void setFilteringCraftable(RecipeBookCategory category, boolean filteringCraftable);

	void setCategoryOptions(RecipeBookCategory category, boolean guiOpen, boolean filteringCraftable);

}
