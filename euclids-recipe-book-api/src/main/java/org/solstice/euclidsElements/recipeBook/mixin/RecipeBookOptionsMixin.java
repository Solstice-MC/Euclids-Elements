package org.solstice.euclidsElements.recipeBook.mixin;

import com.google.common.collect.Maps;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.book.RecipeBookOptions;
import net.minecraft.util.Util;
import org.solstice.euclidsElements.recipeBook.api.EuclidsRecipeBookOptions;
import org.solstice.euclidsElements.recipeBook.api.RecipeBookCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(RecipeBookOptions.class)
public class RecipeBookOptionsMixin implements EuclidsRecipeBookOptions {

	@Unique private final Map<RecipeBookCategory, RecipeBookOptions.CategoryOption> options;

	private RecipeBookOptionsMixin(Map<RecipeBookCategory, RecipeBookOptions.CategoryOption> options) {
		this.options = options;
	}

	@Unique
	private static RecipeBookOptionsMixin create() {
		Map<RecipeBookCategory, RecipeBookOptions.CategoryOption> options = new HashMap<>();
		RecipeBookCategory.streamCategories().forEach(category -> {
			options.put(category, new RecipeBookOptions.CategoryOption(false, false));
		});
		return new RecipeBookOptionsMixin(options);
	}

	@Unique
	private static RecipeBookOptionsMixin fromPacket(PacketByteBuf buf) {
		Map<RecipeBookCategory, RecipeBookOptions.CategoryOption> options = new HashMap<>();
		RecipeBookCategory.streamCategories().forEach(category -> {
			boolean guiOpen = buf.readBoolean();
			boolean filteringCraftable = buf.readBoolean();
			options.put(category, new RecipeBookOptions.CategoryOption(guiOpen, filteringCraftable));
		});
		return new RecipeBookOptionsMixin(options);
	}

	@Unique
	private static RecipeBookOptionsMixin fromNbt(NbtCompound nbt) {
		Map<RecipeBookCategory, RecipeBookOptions.CategoryOption> options = new HashMap<>();
		RecipeBookCategory.streamCategories().forEach(category -> {
			boolean guiOpen = nbt.getBoolean(category.id().toTranslationKey("", "gui_open"));
			boolean filteringCraftable = nbt.getBoolean(category.id().toTranslationKey("", "filtering_craftable"));
			options.put(category, new RecipeBookOptions.CategoryOption(guiOpen, filteringCraftable));
		});
		return new RecipeBookOptionsMixin(options);
	}

	@Override
	public EuclidsRecipeBookOptions copy() {
		Map<RecipeBookCategory, RecipeBookOptions.CategoryOption> options = new HashMap<>();
		RecipeBookCategory.streamCategories().forEach(category -> {
			options.put(category, this.getOptions().get(category));
		});
		return new RecipeBookOptionsMixin(options);
	}





	@Override
	public Map<RecipeBookCategory, RecipeBookOptions.CategoryOption> getOptions() {
		return this.options;
	}





	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		return object instanceof EuclidsRecipeBookOptions recipeOptions && this.getOptions().equals(recipeOptions.getOptions());
	}

	@Override
	public int hashCode() {
		return this.getOptions().hashCode();
	}

}
