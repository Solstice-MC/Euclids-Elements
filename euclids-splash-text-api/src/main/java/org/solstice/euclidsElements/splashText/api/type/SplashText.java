package org.solstice.euclidsElements.splashText.api.type;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.text.Style;
import org.solstice.euclidsElements.util.EuclidsCodecs;

public interface SplashText {

    Codec<SplashText> CODEC = EuclidsCodecs.merge(AdvancedSplashText.CODEC, SimpleSplashText.CODEC);

    SplashTextRenderer getRenderer();

    SplashText setStyle(Style style);

	default int getWeight() {
		return 1;
	}

	default boolean validate() {
		return true;
	}

}
