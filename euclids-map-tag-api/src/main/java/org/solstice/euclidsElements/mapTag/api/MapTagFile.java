package org.solstice.euclidsElements.mapTag.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.tag.TagEntry;
import net.minecraft.util.dynamic.Codecs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record MapTagFile<R> (
	Map<TagEntry, R> entries,
	List<TagEntry> removals
) {

	public static final Codec<TagEntry> ENTRY_CODEC = Codecs.TAG_ENTRY_ID.xmap(id -> new TagEntry(id, true), tagEntry -> null);

	public static <R> Codec<MapTagFile<R>> codec(MapTagKey<?, R> type) {
		return RecordCodecBuilder.create(instance -> instance.group(
			Codec.unboundedMap(TagEntry.CODEC, type.codec()).fieldOf("entries").forGetter(MapTagFile::entries),
			ENTRY_CODEC.listOf().optionalFieldOf("removals", new ArrayList<>()).forGetter(MapTagFile::removals)
		).apply(instance, MapTagFile::new));
	}

}
