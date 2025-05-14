package org.solstice.euclidsElements.api.advancementEvents;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancement.Advancement;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.ResourceManager;
import org.jetbrains.annotations.Nullable;

public class AdvancementEvents {

	public static final Event<Replace> REPLACE = EventFactory.createArrayBacked(Replace.class, listeners -> (key, original, registries) -> {
		for (Replace listener : listeners) {
			@Nullable LootTable replaced = listener.replaceLootTable(key, original, registries);

			if (replaced != null) {
				return replaced;
			}
		}

		return null;
	});

	public static final Event<Modify> MODIFY = EventFactory.createArrayBacked(Modify.class, listeners -> (key, tableBuilder, registries) -> {
		for (Modify listener : listeners) {
			listener.modifyLootTable(key, tableBuilder, registries);
		}
	});

	public static final Event<Loaded> ALL_LOADED = EventFactory.createArrayBacked(Loaded.class, listeners -> (resourceManager, lootManager) -> {
		for (Loaded listener : listeners) {
			listener.onLootTablesLoaded(resourceManager, lootManager);
		}
	});

	@FunctionalInterface
	public interface Replace {
		@Nullable
		LootTable replaceLootTable(RegistryKey<Advancement> key, Advancement original, RegistryWrapper.WrapperLookup registries);
	}

	@FunctionalInterface
	public interface Modify {
		void modifyLootTable(RegistryKey<Advancement> key, Advancement.Builder tableBuilder, RegistryWrapper.WrapperLookup registries);
	}

	@FunctionalInterface
	public interface Loaded {
		void onLootTablesLoaded(ResourceManager resourceManager, Registry<Advancement> registry);
	}

}
