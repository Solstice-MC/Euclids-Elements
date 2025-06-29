package org.solstice.euclidsElements.advancement.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancement.Advancement;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

public class AdvancementEvents {

	public static final Event<Modify> MODIFY = EventFactory.createArrayBacked(Modify.class,
		listeners -> (key, tableBuilder, original, registries) -> {
			for (Modify listener : listeners) listener.modifyLootTable(key, tableBuilder, original, registries);
		}
	);

	@FunctionalInterface
	public interface Modify {
		void modifyLootTable(RegistryKey<Advancement> key, Advancement.Builder builder, Advancement original, RegistryWrapper.WrapperLookup registries);
	}

}
