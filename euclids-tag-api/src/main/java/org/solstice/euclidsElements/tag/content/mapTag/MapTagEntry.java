package org.solstice.euclidsElements.tag.content.mapTag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record MapTagEntry(Identifier id, boolean tag) {

	public static final Codec<MapTagEntry> CODEC = Codec.STRING.comapFlatMap(
		MapTagEntry::fromString, MapTagEntry::asString
	);

	public static DataResult<MapTagEntry> fromString(String entry) {
		return entry.startsWith("#") ?
			Identifier.validate(entry.substring(1)).map(id -> new MapTagEntry(id, true)) :
			Identifier.validate(entry).map(id -> new MapTagEntry(id, false));
	}

	public <T> List<RegistryEntry<T>> getEntries(Registry<T> registry) {
		List<RegistryEntry<T>> result = new ArrayList<>();

		if (this.tag) {
			Optional<TagKey<T>> tag = registry.streamTags()
				.filter(key -> key.id().equals(this.id))
				.findFirst();
			if (tag.isEmpty()) return result;
			registry.getOrCreateEntryList(tag.get()).stream()
				.forEach(result::add);
		} else {
			result.add(registry.getEntry(this.id).orElseThrow());
		}

		return result;
	}

	public String asString() {
		return this.tag ? "#" + this.id : this.id.toString();
	}

	@Override
	public @NotNull String toString() {
		return this.asString();
	}

}
