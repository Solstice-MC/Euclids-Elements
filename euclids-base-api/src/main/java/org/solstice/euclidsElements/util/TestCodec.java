package org.solstice.euclidsElements.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record TestCodec(String test) {

	public static final MapCodec<TestCodec> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.STRING.fieldOf("test").forGetter(TestCodec::test)
	).apply(instance, TestCodec::new));

}
