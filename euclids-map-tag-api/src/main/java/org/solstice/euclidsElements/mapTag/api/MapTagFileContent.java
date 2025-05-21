package org.solstice.euclidsElements.mapTag.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.tag.TagEntry;
import net.minecraft.util.dynamic.Codecs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record MapTagFileContent<R> (
	Map<MapTagEntry, R> entries,
	List<MapTagEntry> removals
) {

	public static final Codec<TagEntry> ENTRY_CODEC = Codecs.TAG_ENTRY_ID.xmap(id -> new TagEntry(id, true), tagEntry -> null);

	public static <R> Codec<MapTagFileContent<R>> codec(MapTagKey<?, R> type) {
		return RecordCodecBuilder.create(instance -> instance.group(
			Codec.unboundedMap(MapTagEntry.CODEC, type.getCodec()).fieldOf("entries").forGetter(MapTagFileContent::entries),
			MapTagEntry.CODEC.listOf().optionalFieldOf("removals", new ArrayList<>()).forGetter(MapTagFileContent::removals)
		).apply(instance, MapTagFileContent::new));
	}

}
