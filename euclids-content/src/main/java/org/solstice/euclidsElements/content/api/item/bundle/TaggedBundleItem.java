package org.solstice.euclidsElements.content.api.item.bundle;

import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;

public class TaggedBundleItem extends BundleItem {

	protected final TagKey<Item> acceptedItems;

	public TaggedBundleItem(Settings settings, TagKey<Item> acceptedItems) {
		super(settings);
		this.acceptedItems = acceptedItems;
	}

	@Override
	public boolean acceptsStack(ItemStack stack) {
		return stack.isIn(this.acceptedItems);
	}

}
