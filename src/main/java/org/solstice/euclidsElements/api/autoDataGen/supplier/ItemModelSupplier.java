package org.solstice.euclidsElements.api.autoDataGen.supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;

public class ItemModelSupplier extends ModelSupplier<Item, ItemModelProvider, ItemModelSupplier.ModelProvider> {

	public static final ItemModelSupplier INSTANCE = new ItemModelSupplier();

	public static void register(Class<? extends Item> clazz, ModelProvider provider) {
		INSTANCE.modelProviders.put(clazz, provider);
	}

	public static void generate(ItemModelProvider generator, Item item, ResourceLocation id) {
		INSTANCE.modelProviders.forEach((clazz, provider) -> {
			if (clazz.isInstance(item)) provider.generate(generator, item, id);
		});
	}

	public interface ModelProvider extends ModelSupplier.ModelProvider<Item, ItemModelProvider> {}

	static {
		register(Item.class, ItemModelSupplier::registerItem);
		register(TieredItem.class, ItemModelSupplier::registerHandheld);
	}

	public static void registerItem(ItemModelProvider generator, Item item, ResourceLocation id) {
		generator.basicItem(item);
	}

	public static void registerHandheld(ItemModelProvider generator, Item item, ResourceLocation id) {
		generator.handheldItem(item);
	}

}
