package org.solstice.tabula.registry;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.content.api.item.bundle.TaggedBundleItem;
import org.solstice.tabula.Tabula;
import org.solstice.tabula.content.item.BreadstickBasketItem;
import org.solstice.tabula.content.item.HumorCheckerItem;
import org.solstice.tabula.content.item.PlayerHumorCheckerItem;
import org.solstice.tabula.content.item.WeightsItem;

import java.util.function.Function;

public class TabulaItems {

	public static void init() {}

	public static final Item WEIGHTS = register("weights", WeightsItem::new);
	public static final Item POCKET_MIRROR = register("pocket_mirror", PlayerHumorCheckerItem::new);
	public static final Item EMERALD_TABLET = register("emerald_tablet", HumorCheckerItem::new);

	public static final Item BREADSTICK_BASKET = register("breadstick_basket", BreadstickBasketItem::new,
		new Item.Settings()
			.maxCount(1)
			.component(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT)
	);

	public static Item register(String name) {
		return register(name, Item::new);
	}

	public static Item register(String name, Function<Item.Settings, Item> function) {
		return register(name, function, new Item.Settings());
	}

	public static Item register(String name, Item.Settings settings) {
		return register(name, Item::new, settings);
	}

	public static Item register(String name, Function<Item.Settings, Item> function, Item.Settings settings) {
		Identifier id = Tabula.of(name);
		RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
		Item item = function.apply(settings);
		return Registry.register(Registries.ITEM, key, item);
	}

}
