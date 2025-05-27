package org.solstice.euclidsElements.splashText.api.type;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public record SimpleSplashText(String text) implements SplashText {

    public static final Codec<SimpleSplashText> CODEC = Codec.STRING.xmap(SimpleSplashText::new, SimpleSplashText::text);

    @Override
    public SplashTextRenderer getRenderer() {
        return new SplashTextRenderer(this.text);
    }

    @Override
    public SplashText setStyle(Style style) {
        return new AdvancedSplashText(Text.literal(text)).setStyle(style);
    }

}
