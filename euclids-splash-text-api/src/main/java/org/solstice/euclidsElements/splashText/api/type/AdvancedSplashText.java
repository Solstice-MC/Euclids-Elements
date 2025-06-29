package org.solstice.euclidsElements.splashText.api.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.dynamic.Codecs;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.splashText.api.EuclidsSplashTextRenderer;
import org.solstice.euclidsElements.util.DateUtils;
import org.solstice.euclidsElements.util.EuclidsCodecs;

import java.time.LocalDate;

public record AdvancedSplashText (
	Text value,
	Integer weight,
	String mod,
	LocalDate date
) implements SplashText {

	public static final Codec<AdvancedSplashText> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		TextCodecs.CODEC.fieldOf("value").forGetter(null),
		Codecs.rangedInt(1, 128).optionalFieldOf("weight", 1).forGetter(null),
		Codec.STRING.optionalFieldOf("mod", "minecraft").forGetter(null),
		EuclidsCodecs.LOCAL_DATE.optionalFieldOf("date", DateUtils.today()).forGetter(null)
	).apply(instance, AdvancedSplashText::new));

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public boolean validate() {
		if (!DateUtils.isToday(this.date)) return false;
		if (!EuclidsElements.isModLoaded(this.mod)) return false;
		return true;
	}

	public AdvancedSplashText(MutableText text) {
		this(text, 1, "minecraft", DateUtils.today());
	}

	@Override
	public SplashTextRenderer getRenderer() {
		return new EuclidsSplashTextRenderer(this.value);
	}

	@Override
	public SplashText setStyle(Style style) {
		return new AdvancedSplashText(
			this.value.copy().fillStyle(style),
			this.weight,
			this.mod,
			this.date
		);
	}

}
