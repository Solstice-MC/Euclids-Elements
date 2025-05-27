package org.solstice.euclidsElements.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EuclidsCodecs {

	private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd MMM yy");

	public static final Codec<LocalDate> LOCAL_DATE = Codec.STRING.xmap(
		string -> LocalDate.parse(string + " 00", FORMAT).withYear(0),
		date -> date.format(FORMAT)
	);

	public static <Type> Codec<Type> merge(
		Codec<? extends Type> codec1,
		Codec<? extends Type> codec2
	) {
		return Codec.either(codec1, codec2).flatComapMap(
			either -> {
				if (either.left().isPresent()) return either.left().get();
				if (either.right().isPresent()) return either.right().get();
				return null;
			}, null
		);
	}

	public static <T> MapCodec<T> merge(
		MapCodec<? extends T> codec1,
		MapCodec<? extends T> codec2
	) {
		return Codec.mapEither(codec1, codec2).xmap(
			either -> {
				if (either.left().isPresent()) return either.left().get();
				if (either.right().isPresent()) return either.right().get();
				return null;
			}, null
		);
	}

}
