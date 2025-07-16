package org.solstice.euclidsElements.splashText.api.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Style;

import java.util.List;
import java.util.stream.Stream;

public record SplashTextFile(
	Style defaultStyle,
	List<SplashText> values
) {

	public static final Codec<SplashTextFile> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Style.Codecs.CODEC.optionalFieldOf("default_style", Style.EMPTY).forGetter(SplashTextFile::defaultStyle),
			SplashText.CODEC.listOf().fieldOf("values").forGetter(SplashTextFile::values)
	).apply(instance, SplashTextFile::new));

	public Stream<SplashText> getTexts() {
		Stream<SplashText> result = values.stream();
		if (defaultStyle != Style.EMPTY) result = result.map(text -> text.setStyle(defaultStyle));
		return result;
	}

}
