package org.solstice.euclidsElements.splashText.api.type;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Style;

import java.util.List;
import java.util.stream.Stream;

public record SplashTextData (
	Style defaultStyle,
	List<SplashText> entries
) {

	public static final Codec<SplashTextData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Style.Codecs.CODEC.optionalFieldOf("default_style", Style.EMPTY).forGetter(SplashTextData::defaultStyle),
			SplashText.CODEC.listOf().fieldOf("entries").forGetter(SplashTextData::entries)
	).apply(instance, SplashTextData::new));

	public static SplashTextData fromJson(JsonObject object) {
		return CODEC.parse(JsonOps.INSTANCE, object).getOrThrow();
	}

	public Stream<SplashText> getTexts() {
		Stream<SplashText> result = entries.stream();
		if (defaultStyle != Style.EMPTY) result = result.map(text -> text.setStyle(defaultStyle));
		return result;
	}

}
