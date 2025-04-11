package org.solstice.euclidsElements.api.autoDataGen.supplier;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class BlockModelSupplier extends ModelSupplier<Block, BlockStateProvider, BlockModelSupplier.ModelProvider> {

	public static final BlockModelSupplier INSTANCE = new BlockModelSupplier();

	public static void register(Class<? extends Block> clazz, BlockModelSupplier.ModelProvider provider) {
		INSTANCE.modelProviders.put(clazz, provider);
	}

	public static void generate(BlockStateProvider generator, Block block, ResourceLocation id) {
		INSTANCE.modelProviders.forEach((clazz, provider) -> {
			if (clazz.isInstance(block)) provider.generate(generator, block, id);
		});
	}

	public interface ModelProvider extends ModelSupplier.ModelProvider<Block, BlockStateProvider> {}

	static {
		register(Block.class, BlockModelSupplier::registerBlock);
//		register(PillarBlock.class, BlockModelSupplier::registerPillar);
//		register(SnowBlock.class, BlockModelSupplier::registerLayered);
//		BLOCK_STATE_PROVIDERS.put(BrazierBlock.class, ACModelProvider::registerBrazier);
//		BLOCK_STATE_PROVIDERS.put(TankBlock.class, ACModelProvider::registerTank);
//		BLOCK_STATE_PROVIDERS.put(PipeBlock.class, ACModelProvider::registerPipe);
//		BLOCK_STATE_PROVIDERS.put(ClusterBlock.class, ACModelProvider::registerCluster);
	}

	public static void registerBlock(BlockStateProvider generator, Block block, ResourceLocation id) {
		ModelFile model = generator.cubeAll(block);
		generator.simpleBlock(block, model);
		generator.simpleBlockItem(block, model);
	}

//	public static void registerPillar(BlockStateProvider generator, Block block, ResourceLocation id) {
//		generator.registerAxisRotated(block, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);
//		generator.registerParentedItemModel(block, id);
//	}

//	public static void registerLayered(BlockStateProvider generator, Block block, ResourceLocation id) {
//		ResourceLocation blockId = id.withPrefixedPath("block/");
//		VariantsBlockStateSupplier supplier = VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.LAYERS).register(height -> {
//			BlockStateVariant variant = BlockStateVariant.create();
//			VariantSetting<ResourceLocation> setting = VariantSettings.MODEL;
//			ResourceLocation path;
//			if (height < 8) path = blockId.withSuffixedPath("/height/" + height * 2);
//			else path = blockId.withSuffixedPath("_block");
//
//			return variant.put(setting, path);
//		}));
//		generator.blockStateCollector.accept(supplier);
//		generator.registerParentedItemModel(block, blockId.withSuffixedPath("/height/2"));
//	}

//	public static void registerPipe(BlockStateModelGenerator generator, Block block, ResourceLocation id) {
//		MultipartBlockStateSupplier supplier = MultipartBlockStateSupplier.create(block)
//			.with(When.create().set(PipeBlock.UP, true), BlockStateVariant.create().put(VariantSettings.MODEL, id.withSuffixedPath("/positive")).put(VariantSettings.X, VariantSettings.Rotation.R270))
//			.with(When.create().set(PipeBlock.DOWN, true), BlockStateVariant.create().put(VariantSettings.MODEL, id.withSuffixedPath("/negative")).put(VariantSettings.X, VariantSettings.Rotation.R90))
//			.with(When.create().set(PipeBlock.NORTH, true), BlockStateVariant.create().put(VariantSettings.MODEL, id.withSuffixedPath("/positive")))
//			.with(When.create().set(PipeBlock.EAST, true), BlockStateVariant.create().put(VariantSettings.MODEL, id.withSuffixedPath("/positive")).put(VariantSettings.Y, VariantSettings.Rotation.R90))
//			.with(When.create().set(PipeBlock.SOUTH, true), BlockStateVariant.create().put(VariantSettings.MODEL, id.withSuffixedPath("/negative")).put(VariantSettings.Y, VariantSettings.Rotation.R180))
//			.with(When.create().set(PipeBlock.WEST, true), BlockStateVariant.create().put(VariantSettings.MODEL, id.withSuffixedPath("/negative")).put(VariantSettings.Y, VariantSettings.Rotation.R270));
//
//		generator.blockStateCollector.accept(supplier);
//		generator.registerParentedBlockModel(block, id);
//	}

//	public static void registerTank(BlockStateModelGenerator generator, Block block, ResourceLocation id) {
//		ResourceLocation blockId = id.withPrefixedPath("block/");
//
//		Models.CUBE_COLUMN.upload(
//			blockId.withSuffixedPath("/default"),
//			TextureMap.sideEnd(blockId.withSuffixedPath("/default"), blockId.withSuffixedPath("/end")),
//			generator.modelCollector
//		);
//		Models.CUBE_COLUMN.upload(
//			blockId.withSuffixedPath("/top"),
//			TextureMap.sideEnd(blockId.withSuffixedPath("/top"), blockId.withSuffixedPath("/end")),
//			generator.modelCollector
//		);
//		Models.CUBE_COLUMN.upload(
//			blockId.withSuffixedPath("/middle"),
//			TextureMap.sideEnd(blockId.withSuffixedPath("/middle"), blockId.withSuffixedPath("/end")),
//			generator.modelCollector
//		);
//		Models.CUBE_COLUMN.upload(
//			blockId.withSuffixedPath("/bottom"),
//			TextureMap.sideEnd(blockId.withSuffixedPath("/bottom"), blockId.withSuffixedPath("/end")),
//			generator.modelCollector
//		);
//
//		VariantsBlockStateSupplier supplier = VariantsBlockStateSupplier.create(block).coordinate(
//			BlockStateVariantMap.create(TankBlock.TOP, TankBlock.BOTTOM)
//				.register(true, true, BlockStateVariant.create()
//					.put(VariantSettings.MODEL, blockId.withSuffixedPath("/default")))
//				.register(true, false, BlockStateVariant.create()
//					.put(VariantSettings.MODEL, blockId.withSuffixedPath("/top")))
//				.register(false, true, BlockStateVariant.create()
//					.put(VariantSettings.MODEL, blockId.withSuffixedPath("/bottom")))
//				.register(false, false, BlockStateVariant.create()
//					.put(VariantSettings.MODEL, blockId.withSuffixedPath("/middle")))
//		);
//
//		generator.blockStateCollector.accept(supplier);
//		generator.registerParentedBlockModel(block, blockId.withSuffixedPath("/default"));
//	}

//	public static void registerBrazier(BlockStateModelGenerator generator, Block block, ResourceLocation id) {
//		ResourceLocation blockId = id.withPrefixedPath("block/");
//
//		Model litModel = new Model(
//			Optional.of(AristotlesComedy.of("block/template/brazier")),
//			Optional.empty(),
//			TextureKey.FIRE
//		);
//		TextureMap litTextures = new TextureMap()
//			.put(TextureKey.FIRE, blockId.withSuffixedPath("_fire"));
//		litModel.upload(block, litTextures, generator.modelCollector);
//
//		Model offModel = new Model(
//			Optional.of(AristotlesComedy.of("block/template/brazier")),
//			Optional.empty(),
//			TextureKey.FIRE
//		);
//		TextureMap offTextures = new TextureMap()
//			.put(TextureKey.FIRE, AristotlesComedy.of("block/empty"));
//		offModel.upload(block, "_off", offTextures, generator.modelCollector);
//
//		VariantsBlockStateSupplier supplier = VariantsBlockStateSupplier.create(block).coordinate(
//			BlockStateVariantMap.create(Properties.LIT)
//				.register(true, BlockStateVariant.create()
//					.put(VariantSettings.MODEL, blockId))
//				.register(false, BlockStateVariant.create()
//					.put(VariantSettings.MODEL, blockId.withSuffixedPath("_off")))
//		);
//
//		generator.blockStateCollector.accept(supplier);
//		generator.registerParentedBlockModel(block, id);
//	}

//	public static void registerCluster(BlockStateModelGenerator generator, Block block, ResourceLocation id) {
//		BlockStateVariant variant = BlockStateVariant.create()
//			.put(VariantSettings.MODEL, Models.CROSS.upload(block, TextureMap.cross(block), generator.modelCollector));
//
//		BlockStateVariantMap stageVariantMap = BlockStateVariantMap.create(ClusterBlock.STAGE).register(i -> {
//			ResourceLocation identifier = generator.createSubModel(block, "/" + i, Models.CROSS, TextureMap::cross);
//			return BlockStateVariant.create().put(VariantSettings.MODEL, identifier);
//		});
//
//		VariantsBlockStateSupplier supplier = VariantsBlockStateSupplier
//			.create(block, variant)
//			.coordinate(generator.createUpDefaultFacingVariantMap())
//			.coordinate(stageVariantMap);
//		generator.blockStateCollector.accept(supplier);
//
//		Models.GENERATED.upload(id.withPrefixedPath("item/"), TextureMap.layer0(id.withPrefixedPath("block/").withSuffixedPath("/0")), generator.modelCollector);
//	}

}
