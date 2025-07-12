package org.solstice.euclidsElements.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record TagEntry(Identifier id, boolean tag) {

	public static final Codec<TagEntry> CODEC = Codec.STRING.comapFlatMap(
		TagEntry::fromString, TagEntry::asString
	);

	public static DataResult<TagEntry> fromString(String entry) {
		return entry.startsWith("#") ?
			Identifier.validate(entry.substring(1)).map(id -> new TagEntry(id, true)) :
			Identifier.validate(entry).map(id -> new TagEntry(id, false));
	}

	public <T> RegistryEntryList<T> getEntries(Registry<T> registry) {
		List<RegistryEntry<T>> result = new ArrayList<>();

		if (this.tag) {
			Optional<TagKey<T>> tag = registry.streamTags()
				.filter(key -> key.id().equals(this.id))
				.findFirst();
			tag.ifPresent(key -> registry.getOrCreateEntryList(key).stream()
				.forEach(result::add)
			);
		} else {
			result.add(registry.getEntry(this.id).orElseThrow());
		}

		return RegistryEntryList.of(result);
	}

	public String asString() {
		return this.tag ? "#" + this.id : this.id.toString();
	}

	@Override
	public @NotNull String toString() {
		return this.asString();
	}

}
