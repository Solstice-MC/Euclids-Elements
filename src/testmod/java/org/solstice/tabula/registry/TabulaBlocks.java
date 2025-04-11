package org.solstice.tabula.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.solstice.tabula.Tabula;

import java.util.function.Function;

public class TabulaBlocks {

	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.Blocks.createBlocks(Tabula.MOD_ID);


	public static final DeferredBlock<Block> YELLOW_TRISMEGISTITE_BLOCK = register("yellow_trismegistite_block", BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK));


	public static DeferredBlock<Block> register(String name, BlockBehaviour.Properties settings) {
		return register(name, Block::new, settings);
	}

	public static DeferredBlock<Block> register(String name, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties settings) {
		return register(name, function, settings, new Item.Properties());
	}

	public static DeferredBlock<Block> register(String name, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties blockSettings, Item.Properties itemSettings) {
		DeferredBlock<Block> block = REGISTRY.registerBlock(name, function, blockSettings);
		TabulaItems.REGISTRY.registerSimpleBlockItem(block, itemSettings);
		return block;
    }

}
