package org.solstice.euclidsElements.tag.content.mapTag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.solstice.euclidsElements.tag.api.MapTagKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record MapTagData<R> (
	Map<MapTagEntry, R> entries,
	List<MapTagEntry> removals,
	boolean replace
) {

	public static <R> Codec<MapTagData<R>> codec(MapTagKey<?, R> type) {
		return RecordCodecBuilder.create(instance -> instance.group(
			Codec.unboundedMap(MapTagEntry.CODEC, type.getCodec()).fieldOf("entries").forGetter(MapTagData::entries),
			MapTagEntry.CODEC.listOf().optionalFieldOf("removals", new ArrayList<>()).forGetter(MapTagData::removals),
			Codec.BOOL.optionalFieldOf("replace", false).forGetter(MapTagData::replace)
		).apply(instance, MapTagData::new));
	}

}
