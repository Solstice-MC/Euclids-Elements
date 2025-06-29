package org.solstice.euclidsElements.recipeBook.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.packet.CustomPayload;
import org.solstice.euclidsElements.EuclidsElements;

public record SyncRecipeCategoryOptionsPacket(
	RecipeBookCategory category,
	boolean guiOpen,
	boolean filteringCraftable
) implements CustomPayload {

	public static final CustomPayload.Id<SyncRecipeCategoryOptionsPacket> ID = new CustomPayload.Id<>(EuclidsElements.of("sync_recipe_category_options"));

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static final Codec<SyncRecipeCategoryOptionsPacket> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		RecipeBookCategory.CODEC.fieldOf("category").forGetter(SyncRecipeCategoryOptionsPacket::category),
		Codec.BOOL.fieldOf("gui_open").forGetter(SyncRecipeCategoryOptionsPacket::guiOpen),
		Codec.BOOL.fieldOf("gui_open").forGetter(SyncRecipeCategoryOptionsPacket::guiOpen)
	).apply(instance, SyncRecipeCategoryOptionsPacket::new));

}
