package org.solstice.tabula.registry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.content.test.ConstructedRegistryContainer;
import org.solstice.tabula.Tabula;

import java.util.function.Function;

public class TabulaBlocks {

	protected static final ConstructedRegistryContainer<Block, Block.Settings> CONTAINER = Tabula.REGISTRY.block();

	public static void init() {}

	public static final Block PERFECTLY_GENERIC_OBJECT = CONTAINER.register("perfectly_generic_object", Block::new,
		AbstractBlock.Settings.copy(Blocks.STONE)
	);

}
