package org.solstice.euclidsElements.util;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public record CodecRecipeSerializer<T extends Recipe<?>> (
        MapCodec<T> codec
) implements RecipeSerializer<T> {

	@Override
	public @NotNull StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
		return ByteBufCodecs.fromCodecWithRegistries(codec.codec());
	}

}
