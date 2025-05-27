package org.solstice.euclidsElements.splashText.api;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class EuclidsSplashTextRenderer extends SplashTextRenderer {

    public final Text text;

    public EuclidsSplashTextRenderer(Text text) {
        super("");
        this.text = text;
    }

    public void render(DrawContext drawContext, int i, TextRenderer textRenderer, int j) {
        drawContext.getMatrices().push();
        drawContext.getMatrices().translate((float)i / 2.0F + 123.0F, 69.0F, 0.0F);
        drawContext.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-20.0F));
        float f = 1.8F - MathHelper.abs(MathHelper.sin((float)(Util.getMeasuringTimeMs() % 1000L) / 1000.0F * 6.2831855F) * 0.1F);
        f = f * 100.0F / (float)(textRenderer.getWidth(this.text) + 32);
        drawContext.getMatrices().scale(f, f, f);
        drawContext.drawCenteredTextWithShadow(textRenderer, this.text, 0, -8, 16776960 | j);
        drawContext.getMatrices().pop();
    }

}
