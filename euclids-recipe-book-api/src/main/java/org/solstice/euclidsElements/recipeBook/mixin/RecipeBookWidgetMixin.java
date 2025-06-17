package org.solstice.euclidsElements.recipeBook.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.network.packet.c2s.play.RecipeCategoryOptionsC2SPacket;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.CraftingScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RecipeBookWidget.class)
public class RecipeBookWidgetMixin {

	@Shadow protected CraftingScreenHandler craftingScreenHandler;
	@Shadow private ClientRecipeBook recipeBook;

	@Shadow
	protected MinecraftClient client;


	@WrapOperation('toggleFilteringCraftable')
	private boolean toggleFilteringCraftable() {
		RecipeBookCategory recipeBookCategory = this.craftingScreenHandler.getCategory();
		boolean bl = !this.recipeBook.isFilteringCraftable(recipeBookCategory);
		this.recipeBook.setFilteringCraftable(recipeBookCategory, bl);
		return bl;
	}

	@WrapOperation('sendBookDataPacket')
	protected void sendBookDataPacket() {
		if (this.client.getNetworkHandler() == null) return;

		RecipeBookCategory recipeBookCategory = this.craftingScreenHandler.getCategory();
		boolean bl = this.recipeBook.getOptions().isGuiOpen(recipeBookCategory);
		boolean bl2 = this.recipeBook.getOptions().isFilteringCraftable(recipeBookCategory);
		this.client.getNetworkHandler().sendPacket(new RecipeCategoryOptionsC2SPacket(recipeBookCategory, bl, bl2));
	}


}
