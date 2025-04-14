package org.solstice.euclidsElements.api.autoDataGen.supplier;

import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ItemModelSupplier extends ModelSupplier<Item, ItemModelGenerator, ItemModelSupplier.ModelProvider> {

	public static final ItemModelSupplier INSTANCE = new ItemModelSupplier();

	public static void register(Class<? extends Item> clazz, ModelProvider provider) {
		INSTANCE.modelProviders.addFirst(Map.entry(clazz, provider));
	}

	public static void generate(ItemModelGenerator generator, Item item, Identifier id) {
		for (Map.Entry<Class<? extends Item>, ItemModelSupplier.ModelProvider> entry : INSTANCE.modelProviders) {
			Class<? extends Item> clazz = entry.getKey();
			ItemModelSupplier.ModelProvider provider = entry.getValue();
			if (clazz.isInstance(item)) {
				provider.generate(generator, item, id);
				break;
			}
		}
	}

	public interface ModelProvider extends ModelSupplier.ModelProvider<Item, ItemModelGenerator> {}

	static {
		register(Item.class, ItemModelSupplier::registerItem);
		register(BlockItem.class, ItemModelSupplier::registerNothing);
		register(ToolItem.class, ItemModelSupplier::registerHandheld);
	}

	public static void registerNothing(ItemModelGenerator generator, Item item, Identifier id) {}

	public static void registerItem(ItemModelGenerator generator, Item item, Identifier id) {
		generator.register(item, Models.GENERATED);
	}

	public static void registerHandheld(ItemModelGenerator generator, Item item, Identifier id) {
		generator.register(item, Models.HANDHELD);
	}

}
