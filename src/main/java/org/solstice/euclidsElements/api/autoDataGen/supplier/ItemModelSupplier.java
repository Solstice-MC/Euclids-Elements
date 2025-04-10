package org.solstice.euclidsElements.api.autoDataGen.supplier;

import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Identifier;

public class ItemModelSupplier extends ModelSupplier<Item, ItemModelGenerator, ItemModelSupplier.ModelProvider> {

	public static final ItemModelSupplier INSTANCE = new ItemModelSupplier();

	public static void register(Class<? extends Item> clazz, ModelProvider provider) {
		INSTANCE.modelProviders.put(clazz, provider);
	}

	public static void generate(ItemModelGenerator generator, Item item, Identifier id) {
		INSTANCE.modelProviders.forEach((clazz, provider) -> {
			if (clazz.isInstance(item)) provider.generate(generator, item, id);
		});
	}

	public interface ModelProvider extends ModelSupplier.ModelProvider<Item, ItemModelGenerator> {}

	static {
		register(Item.class, ItemModelSupplier::registerItem);
		register(ToolItem.class, ItemModelSupplier::registerHandheld);
	}

	public static void registerItem(ItemModelGenerator generator, Item item, Identifier id) {
		generator.register(item, Models.GENERATED);
	}

	public static void registerHandheld(ItemModelGenerator generator, Item item, Identifier id) {
		generator.register(item, Models.HANDHELD);
	}

}
