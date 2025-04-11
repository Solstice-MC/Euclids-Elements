package org.solstice.tabula.registry;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.solstice.tabula.Tabula;

import java.util.function.Function;

public class TabulaItems {

	public static final DeferredRegister.Items REGISTRY = DeferredRegister.Items.createItems(Tabula.MOD_ID);


	public static final DeferredItem<Item> RAW_TIN = register("raw_tin");


	public static DeferredItem<Item> register(String name) {
		return register(name, Item::new);
	}

	public static DeferredItem<Item> register(String name, Function<Item.Properties, Item> function) {
		return register(name, function, new Item.Properties());
	}

	public static DeferredItem<Item> register(String name, Item.Properties settings) {
		return register(name, Item::new, settings);
	}

	public static DeferredItem<Item> register(String name, Function<Item.Properties, Item> function, Item.Properties settings) {
        return REGISTRY.registerItem(name, function, settings);
    }

}
