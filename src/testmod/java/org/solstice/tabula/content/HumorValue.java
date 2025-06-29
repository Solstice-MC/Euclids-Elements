package org.solstice.tabula.content;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

public enum HumorValue implements StringIdentifiable {

	QUITE("quite"),
	MEDIOCRE("mediocre"),
	UNSATISFACTORY("unsatisfactory");

	public static final Codec<HumorValue> CODEC = StringIdentifiable.createCodec(HumorValue::values);

	private final String value;

	HumorValue(String value) {
		this.value = value;
	}

	private static final HumorValue[] VALUES = values();

	public HumorValue next() {
		return VALUES[(this.ordinal() + 1) % VALUES.length];
	}

	@Override
	public String asString() {
		return this.value;
	}

}
