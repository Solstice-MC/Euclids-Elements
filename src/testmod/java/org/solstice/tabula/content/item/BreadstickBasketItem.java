package org.solstice.tabula.content.item;

import org.solstice.euclidsElements.content.api.item.bundle.TaggedBundleItem;
import org.solstice.tabula.registry.TabulaTags;

public class BreadstickBasketItem extends TaggedBundleItem {

	public BreadstickBasketItem(Settings settings) {
		super(settings, TabulaTags.BREAD_AND_STICKS);
	}

	@Override
	public int getCapacity() {
		return 127;
	}

}
