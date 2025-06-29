package org.solstice.euclidsElements.tag.content.mapTag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.solstice.euclidsElements.tag.api.MapTagKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record MapTagFile<R> (
	Map<MapTagEntry, R> entries,
	List<MapTagEntry> removals,
	boolean replace
) {

	public static <T, R> Codec<MapTagFile<R>> codec(MapTagKey<T, R> type) {
		return RecordCodecBuilder.create(instance -> instance.group(
			Codec.unboundedMap(MapTagEntry.CODEC, type.getCodec()).fieldOf("entries").forGetter(MapTagFile::entries),
			MapTagEntry.CODEC.listOf().optionalFieldOf("removals", new ArrayList<>()).forGetter(MapTagFile::removals),
			Codec.BOOL.optionalFieldOf("replace", false).forGetter(MapTagFile::replace)
		).apply(instance, MapTagFile::new));
	}

}
