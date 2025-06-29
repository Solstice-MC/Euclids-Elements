package org.solstice.euclidsElements.audio.mixin;

import net.minecraft.client.sound.SoundManager;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(SoundManager.SoundList.class)
public class SoundListMixin {

	@Shadow private Map<Identifier, Resource> foundSounds;

	/**
	 * @author Sindercube
	 * @reason Find all audio files
	 */
	@Overwrite
	public void findSounds(ResourceManager manager) {
		this.foundSounds = manager.findResources("sounds", i -> true);
	}

}
