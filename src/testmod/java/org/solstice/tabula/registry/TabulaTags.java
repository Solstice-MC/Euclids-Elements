package org.solstice.tabula.registry;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.solstice.euclidsElements.content.test.ConstructedRegistryContainer;
import org.solstice.euclidsElements.tag.api.MapTagKey;
import org.solstice.tabula.Tabula;
import org.solstice.tabula.content.HumorValue;

public class TabulaTags {

	public static void init() {}

	public static final TagKey<Item> BREAD_AND_STICKS = of("bread_and_sticks", RegistryKeys.ITEM);
	public static final MapTagKey<Block, HumorValue> HUMOROUS = ofMap("humorous", RegistryKeys.BLOCK, HumorValue.CODEC);

	public static <T> TagKey<T> of(String name, RegistryKey<Registry<T>> registry) {
		return TagKey.of(registry, Tabula.of(name));
	}

	public static <T, R> MapTagKey<T, R> ofMap(String name, RegistryKey<Registry<T>> registry, Codec<R> codec) {
		return MapTagKey.of(registry, codec, Tabula.of(name));
	}

}
