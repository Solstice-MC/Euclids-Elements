package org.solstice.euclidsElements.recipeBook.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.book.RecipeBookOptions;

import java.util.Map;

public interface EuclidsRecipeBookOptions {

	Map<RecipeBookCategory, RecipeBookOptions.CategoryOption> getOptions();








	default boolean isGuiOpen(RecipeBookCategory category) {
		return this.getOptions().get(category).guiOpen;
	}

	default void setGuiOpen(RecipeBookCategory category, boolean open) {
		this.getOptions().get(category).guiOpen = open;
	}

	default boolean isFilteringCraftable(RecipeBookCategory category) {
		return this.getOptions().get(category).filteringCraftable;
	}

	default void setFilteringCraftable(RecipeBookCategory category, boolean filtering) {
		this.getOptions().get(category).filteringCraftable = filtering;
	}

	default void toPacket(PacketByteBuf buf) {
		RecipeBookCategory.streamCategories().forEach(category -> {
			RecipeBookOptions.CategoryOption option = this.getOptions().get(category);
			if (option == null) {
				buf.writeBoolean(false);
				buf.writeBoolean(false);
			} else {
				buf.writeBoolean(option.guiOpen);
				buf.writeBoolean(option.filteringCraftable);
			}
		});
	}


	default void writeNbt(NbtCompound nbt) {
		RecipeBookCategory.streamCategories().forEach(category -> {
			RecipeBookOptions.CategoryOption option = this.getOptions().get(category);
			nbt.putBoolean(category.id().toTranslationKey("", "gui_open"), option.guiOpen);
			nbt.putBoolean(category.id().toTranslationKey("", "filtering_craftable"), option.filteringCraftable);
		});
	}

	EuclidsRecipeBookOptions copy();

	default void copyFrom(RecipeBookOptions other) {
		this.getOptions().clear();
		RecipeBookCategory.streamCategories().forEach(category -> {
			RecipeBookOptions.CategoryOption option = other.getOptions().get(category);
			this.getOptions().put(category, option.copy());
		});
	}

}
