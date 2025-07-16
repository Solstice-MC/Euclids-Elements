package org.solstice.euclidsElements.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class EuclidsCodecs {

	private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd MMM yy");

	public static final Codec<LocalDate> LOCAL_DATE = Codec.STRING.xmap(
		string -> LocalDate.parse(string + " 00", FORMAT).withYear(0),
		date -> date.format(FORMAT)
	);

	@SuppressWarnings("unchecked")
	public static <T> Codec<T> merge(Codec<? extends T> primary, Codec<? extends T> alternative) {
		return new MergedCodec<>((Codec<T>) primary, (Codec<T>) alternative);
	}

	@SuppressWarnings("unchecked")
	public static <T> Codec<T> merge(Codec<? extends T> primary, Codec<? extends T>... alternatives) {
		return new MergedCodec<>((Codec<T>) primary, (Codec<T>[]) alternatives);
	}

	public record MergedCodec<T>(Codec<T> primary, Codec<T>... alternatives) implements Codec<T> {

		@SafeVarargs
		public MergedCodec {}

		@Override
		public <T1> DataResult<T1> encode(T input, DynamicOps<T1> ops, T1 prefix) {
			DataResult<T1> result = DataResult.error(() -> "Failed to merge codecs");

			try {
				result = primary.encode(input, ops, prefix)
					.ifError(error -> {});
				if (result.isSuccess()) return result;
			} catch (Exception ignored) {}


			for (Codec<T> alternative : alternatives) {
				try {
					result = alternative.encode(input, ops, prefix)
						.ifError(error -> {});
					if (result.isSuccess()) return result;
				} catch (Exception ignored) {}
			}

			return result;
		}

		@Override
		public <T1> DataResult<Pair<T, T1>> decode(DynamicOps<T1> ops, T1 input) {
			DataResult<Pair<T, T1>> result = DataResult.error(() -> "Failed to merge codecs");

			try {
				result = primary.decode(ops, input)
					.ifError(error -> {});
				if (result.isSuccess()) return result;
			} catch (Exception ignored) {}

			for (Codec<T> alternative : alternatives) {
				try {
					result = alternative.decode(ops, input)
						.ifError(error -> {
						})
						.map(pair -> pair.mapFirst(Function.identity()));
					if (result.isSuccess()) return result;
				} catch (Exception ignored) {}
			}

			return result;
		}

	}

}
