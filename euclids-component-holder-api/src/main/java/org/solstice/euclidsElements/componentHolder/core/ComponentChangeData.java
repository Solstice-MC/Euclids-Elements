package org.solstice.euclidsElements.componentHolder.core;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentMap;
import net.minecraft.resource.Resource;
import org.solstice.euclidsElements.util.TagEntry;

import java.io.IOException;

public record ComponentChangeData(
	TagEntry target,
	ComponentMap components
) {

	public static final Codec<ComponentChangeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		TagEntry.CODEC.fieldOf("target").forGetter(ComponentChangeData::target),
		ComponentMap.CODEC.fieldOf("components").forGetter(ComponentChangeData::components)
	).apply(instance, ComponentChangeData::new));

	public static ComponentChangeData fromResource(Resource resource) {
		try {
			JsonElement element = JsonParser.parseReader(resource.getReader());
			return CODEC.decode(JsonOps.INSTANCE, element).getOrThrow().getFirst();
		} catch (IOException ignored) {
			return null;
		}
	}

}
