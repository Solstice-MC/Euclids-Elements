package org.solstice.euclidsElements.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import javax.annotation.Nullable;
import java.util.Optional;

public class RegistryHelper {

	@SuppressWarnings("unchecked")
	public static <T> HolderSet<T> getTagValues (
		@Nullable HolderLookup.Provider lookup,
		ResourceKey<Registry<T>> registry,
		TagKey<T> tag
	) {
		if (lookup != null) {
			Optional<HolderSet.Named<T>> result = lookup.lookupOrThrow(registry).get(tag);
			if (result.isPresent()) return result.get();
		}

		return HolderSet.direct(new Holder[]{});
	}

}
