package org.solstice.euclidsElements.audio.mixin;

import net.minecraft.client.sound.Sound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Sound.class)
public class SoundMixin {

	@Shadow @Final private Identifier id;

	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static Identifier defaultExtension(Identifier id) {
		if (id.getPath().matches(".*\\..*")) return id;
		return id.withSuffixedPath(".ogg");
	}

	/**
	 * @author Solstice
	 * @reason No extension identifiers
	 */
	@Overwrite
	public Identifier getLocation() {
		return this.id.withPrefixedPath("sounds/");
	}

}
