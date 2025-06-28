package org.solstice.euclidsElements.tag.content;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.tag.TagEntry;

import java.util.ArrayList;
import java.util.List;

public record RemovalTagFile(List<TagEntry> removals) {

	public static final Codec<RemovalTagFile> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		TagEntry.CODEC.listOf().optionalFieldOf("removals", new ArrayList<>()).forGetter(RemovalTagFile::removals)
	).apply(instance, RemovalTagFile::new));

}
