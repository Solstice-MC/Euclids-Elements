package org.solstice.euclidsElements.tag.content.mapTag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.solstice.euclidsElements.tag.api.MapTagKey;
import org.solstice.euclidsElements.util.TagEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record MapTagFile<R> (
	Map<TagEntry, R> entries,
	List<TagEntry> removals,
	boolean replace
) {

	public static <T, R> Codec<MapTagFile<R>> codec(MapTagKey<T, R> type) {
		return RecordCodecBuilder.create(instance -> instance.group(
			Codec.unboundedMap(TagEntry.CODEC, type.getCodec()).fieldOf("entries").forGetter(MapTagFile::entries),
			TagEntry.CODEC.listOf().optionalFieldOf("removals", new ArrayList<>()).forGetter(MapTagFile::removals),
			Codec.BOOL.optionalFieldOf("replace", false).forGetter(MapTagFile::replace)
		).apply(instance, MapTagFile::new));
	}

}
