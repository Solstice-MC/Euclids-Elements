package org.solstice.tabula.registry;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.solstice.euclidsElements.mapTag.api.MapTagKey;
import org.solstice.tabula.Tabula;
import org.solstice.tabula.content.HumorValue;

public class TabulaTags {

	public static final MapTagKey<Block, HumorValue> HUMOROUS = ofMap(RegistryKeys.BLOCK, HumorValue.CODEC, "humorous");

	public static <T, R> MapTagKey<T, R> ofMap(RegistryKey<Registry<T>> registry, Codec<R> codec, String name) {
		return MapTagKey.of(registry, codec, Tabula.of(name));
	}

}
