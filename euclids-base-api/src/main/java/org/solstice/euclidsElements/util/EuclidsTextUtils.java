package org.solstice.euclidsElements.util;

import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class EuclidsTextUtils {

	public static Text translateEntry(RegistryEntryList<?> entryList) {
		String type = entryList.stream().findFirst().orElseThrow().getKey().orElseThrow().getRegistry().getPath();
		return translateEntry(entryList, type);
	}

	public static Text translateEntry(RegistryEntryList<?> entryList, String type) {
		if (entryList.getTagKey().isPresent())
			return Text.translatable(entryList.getTagKey().get().getTranslationKey());

		MutableText result = Text.empty();
		entryList.stream().forEach(entry -> {
			MutableText translation = Text.translatable(
				entry.getKey().orElseThrow().getValue().toTranslationKey(type)
			);
			result.append(translation);
		});
		return result;
	}

}
