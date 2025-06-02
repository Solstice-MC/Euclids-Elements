package org.solstice.euclidsElements.autoDatagen.api.supplier;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Optional;

public class BlockModelSupplier extends ModelSupplier<Block, BlockStateModelGenerator, BlockModelSupplier.ModelProvider> {

	public static final BlockModelSupplier INSTANCE = new BlockModelSupplier();

	public static void register(Class<? extends Block> clazz, BlockModelSupplier.ModelProvider provider) {
		INSTANCE.modelProviders.addFirst(Map.entry(clazz, provider));
	}

	public static void generate(BlockStateModelGenerator generator, Block item, Identifier id) {
		for (Map.Entry<Class<? extends Block>, BlockModelSupplier.ModelProvider> entry : INSTANCE.modelProviders) {
			Class<? extends Block> clazz = entry.getKey();
			BlockModelSupplier.ModelProvider provider = entry.getValue();
			if (clazz.isInstance(item)) {
				provider.generate(generator, item, id);
				break;
			}
		}
	}

	public static Model templatedModel(Identifier id, Optional<String> variant, TextureKey... keys) {
		id = id.withPrefixedPath("block/template/");
		return new Model(Optional.of(id), variant, keys);
	}

	public static Model templatedModel(Identifier id, Optional<String> variant) {
		return templatedModel(id, variant, TextureKey.TEXTURE);
	}

	public static Model templatedModel(Identifier id) {
		return templatedModel(id, Optional.empty(), TextureKey.TEXTURE);
	}

	public interface ModelProvider extends ModelSupplier.ModelProvider<Block, BlockStateModelGenerator> {}

	static {
		register(Block.class, BlockModelSupplier::registerBlock);
		register(PillarBlock.class, BlockModelSupplier::registerPillar);
		register(SnowBlock.class, BlockModelSupplier::registerLayered);
	}

	public static void registerBlock(BlockStateModelGenerator generator, Block block, Identifier id) {
		generator.registerSimpleCubeAll(block);
		generator.registerParentedItemModel(block, id.withPrefixedPath("block/"));
	}

	public static void registerPillar(BlockStateModelGenerator generator, Block block, Identifier id) {
		generator.registerAxisRotated(block, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);
		generator.registerParentedItemModel(block, id);
	}

	public static void registerLayered(BlockStateModelGenerator generator, Block block, Identifier id) {
		Identifier blockId = id.withPrefixedPath("block/");
		VariantsBlockStateSupplier supplier = VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.LAYERS).register(height -> {
			BlockStateVariant variant = BlockStateVariant.create();
			VariantSetting<Identifier> setting = VariantSettings.MODEL;
			Identifier path;
			if (height < 8) path = blockId.withSuffixedPath("/height/" + height * 2);
			else path = blockId.withSuffixedPath("_block");

			return variant.put(setting, path);
		}));
		generator.blockStateCollector.accept(supplier);
		generator.registerParentedItemModel(block, blockId.withSuffixedPath("/height/2"));
	}

	public static void registerCooker(BlockStateModelGenerator generator, Block block, Identifier id) {
		generator.registerCooker(block, TexturedModel.ORIENTABLE);
		generator.registerParentedItemModel(block, id);
	}

	public static void registerCubeTop(BlockStateModelGenerator generator, Block block, Identifier id) {
		generator.registerSingleton(block, TexturedModel.CUBE_TOP);
		generator.registerParentedItemModel(block, id);
	}

}
