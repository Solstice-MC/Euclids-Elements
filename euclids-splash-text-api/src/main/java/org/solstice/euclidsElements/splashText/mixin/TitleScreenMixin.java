package org.solstice.euclidsElements.splashText.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

	@Shadow @Nullable private SplashTextRenderer splashText;

	protected TitleScreenMixin(Text title) {
		super(title);
	}

	@Inject(method = "mouseClicked", at = @At("RETURN"))
	private void getNewSplash(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
		if (mouseY > (double) this.height / 3) return;

		assert this.client != null;
		this.splashText = this.client.getSplashTextLoader().get();
		this.client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

}
