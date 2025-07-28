package org.solstice.tabula.registry;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.Item;
import org.solstice.euclidsElements.content.test.ConstructedRegistryContainer;
import org.solstice.tabula.Tabula;
import org.solstice.tabula.content.item.BreadstickBasketItem;
import org.solstice.tabula.content.item.HumorCheckerItem;
import org.solstice.tabula.content.item.PlayerHumorCheckerItem;
import org.solstice.tabula.content.item.WeightsItem;

public class TabulaItems {

	protected static final ConstructedRegistryContainer<Item, Item.Settings> CONTAINER = Tabula.REGISTRY.item();

	public static void init() {}

	public static final Item WEIGHTS = CONTAINER.register("weights", WeightsItem::new);
	public static final Item POCKET_MIRROR = CONTAINER.register("pocket_mirror", PlayerHumorCheckerItem::new);
	public static final Item EMERALD_TABLET = CONTAINER.register("emerald_tablet", HumorCheckerItem::new);

	public static final Item BREADSTICK_BASKET = CONTAINER.register("breadstick_basket", BreadstickBasketItem::new,
		new Item.Settings()
			.maxCount(1)
			.component(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT)
	);

}
