package org.solstice.euclidsElements.tag.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.tag.TagEntry;
import net.minecraft.util.dynamic.Codecs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record MapTagData<R> (
	Map<MapTagEntry, R> entries,
	List<MapTagEntry> removals
) {

	public static final Codec<TagEntry> ENTRY_CODEC = Codecs.TAG_ENTRY_ID.xmap(id -> new TagEntry(id, true), tagEntry -> null);

	public static <R> Codec<MapTagData<R>> codec(MapTagKey<?, R> type) {
		return RecordCodecBuilder.create(instance -> instance.group(
			Codec.unboundedMap(MapTagEntry.CODEC, type.getCodec()).fieldOf("entries").forGetter(MapTagData::entries),
			MapTagEntry.CODEC.listOf().optionalFieldOf("removals", new ArrayList<>()).forGetter(MapTagData::removals)
		).apply(instance, MapTagData::new));
	}

}
