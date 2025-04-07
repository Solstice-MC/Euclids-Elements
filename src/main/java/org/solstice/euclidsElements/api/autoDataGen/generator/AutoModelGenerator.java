package org.solstice.euclidsElements.api.autoDataGen.generator;

import net.minecraft.data.DataOutput;
import net.minecraft.data.client.*;

public class AutoModelGenerator extends ModelProvider {

	public AutoModelGenerator(DataOutput output) {
		super(output);
	}

//	public AutoModelGenerator(FabricDataOutput output) {
//		super(output);
//	}
//
//	@FunctionalInterface
//	public interface BlockStateProvider {
//		void generate(BlockStateModelGenerator generate, Block block, Identifier id);
//	}
//
//	public static Map<Class<? extends Block>, BlockStateProvider> BLOCK_STATE_PROVIDERS = new LinkedHashMap<>();
//
//	static {
//		BLOCK_STATE_PROVIDERS.put(BrazierBlock.class, ACModelProvider::registerBrazier);
//		BLOCK_STATE_PROVIDERS.put(TankBlock.class, ACModelProvider::registerTank);
//		BLOCK_STATE_PROVIDERS.put(PipeBlock.class, ACModelProvider::registerPipe);
//		BLOCK_STATE_PROVIDERS.put(PillarBlock.class, ACModelProvider::registerPillar);
//		BLOCK_STATE_PROVIDERS.put(SnowBlock.class, ACModelProvider::registerLayered);
//		BLOCK_STATE_PROVIDERS.put(ClusterBlock.class, ACModelProvider::registerCluster);
//		BLOCK_STATE_PROVIDERS.put(Block.class, ACModelProvider::registerBlock);
//	}
//
//	public static void registerPipe(BlockStateModelGenerator generator, Block block, Identifier id) {
//		MultipartBlockStateSupplier supplier = MultipartBlockStateSupplier.create(block)
//			.with(When.create().set(PipeBlock.UP, true), BlockStateVariant.create().put(VariantSettings.MODEL, id.withSuffixedPath("/positive")).put(VariantSettings.X, VariantSettings.Rotation.R270))
//			.with(When.create().set(PipeBlock.DOWN, true), BlockStateVariant.create().put(VariantSettings.MODEL, id.withSuffixedPath("/negative")).put(VariantSettings.X, VariantSettings.Rotation.R90))
//			.with(When.create().set(PipeBlock.NORTH, true), BlockStateVariant.create().put(VariantSettings.MODEL, id.withSuffixedPath("/positive")))
//			.with(When.create().set(PipeBlock.EAST, true), BlockStateVariant.create().put(VariantSettings.MODEL, id.withSuffixedPath("/positive")).put(VariantSettings.Y, VariantSettings.Rotation.R90))
//			.with(When.create().set(PipeBlock.SOUTH, true), BlockStateVariant.create().put(VariantSettings.MODEL, id.withSuffixedPath("/negative")).put(VariantSettings.Y, VariantSettings.Rotation.R180))
//			.with(When.create().set(PipeBlock.WEST, true), BlockStateVariant.create().put(VariantSettings.MODEL, id.withSuffixedPath("/negative")).put(VariantSettings.Y, VariantSettings.Rotation.R270));
//
//		generator.blockStateCollector.accept(supplier);
//		generator.registerParentedItemModel(block, id);
//	}
//
//	public static void registerTank(BlockStateModelGenerator generator, Block block, Identifier id) {
//		Identifier blockId = id.withPrefixedPath("block/");
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
//		generator.registerParentedItemModel(block, blockId.withSuffixedPath("/default"));
//	}
//
//	public static void registerBrazier(BlockStateModelGenerator generator, Block block, Identifier id) {
//		Identifier blockId = id.withPrefixedPath("block/");
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
//		generator.registerParentedItemModel(block, id);
//	}
//
//	public static void registerPillar(BlockStateModelGenerator generator, Block block, Identifier id) {
//		generator.registerAxisRotated(block, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);
//		generator.registerParentedItemModel(block, id);
//	}
//
//	public static void registerLayered(BlockStateModelGenerator generator, Block block, Identifier id) {
//		Identifier blockId = id.withPrefixedPath("block/");
//		VariantsBlockStateSupplier supplier = VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(Properties.LAYERS).register(height -> {
//			BlockStateVariant variant = BlockStateVariant.create();
//			VariantSetting<Identifier> setting = VariantSettings.MODEL;
//			Identifier path;
//			if (height < 8) path = blockId.withSuffixedPath("/height/" + height * 2);
//			else path = blockId.withSuffixedPath("_block");
//
//			return variant.put(setting, path);
//		}));
//		generator.blockStateCollector.accept(supplier);
//		generator.registerParentedItemModel(block, blockId.withSuffixedPath("/height/2"));
//	}
//
//	public static void registerCluster(BlockStateModelGenerator generator, Block block, Identifier id) {
//		BlockStateVariant variant = BlockStateVariant.create()
//			.put(VariantSettings.MODEL, Models.CROSS.upload(block, TextureMap.cross(block), generator.modelCollector));
//
//		BlockStateVariantMap stageVariantMap = BlockStateVariantMap.create(ClusterBlock.STAGE).register(i -> {
//			Identifier identifier = generator.createSubModel(block, "/" + i, Models.CROSS, TextureMap::cross);
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
//
//	public static void registerBlock(BlockStateModelGenerator generator, Block block, Identifier id) {
//		generator.registerSimpleCubeAll(block);
//		generator.registerParentedItemModel(block, id.withPrefixedPath("block/"));
//	}
//
//	@Override
//	public void generateBlockStateModels(BlockStateModelGenerator generator) {
//		Registries.BLOCK.streamEntries()
//			.filter(AristotlesComedyDataGenerator::ownsRegistry)
//			.forEach(entry -> this.generateBlockModel(generator, entry));
//	}
//
//	public void generateBlockModel(BlockStateModelGenerator generator, RegistryEntry<Block> entry) {
//		Block block = entry.value();
//		Identifier id = entry.getKeyOrValue().left().orElseThrow().getValue();
//
//		for (Map.Entry<Class<? extends Block>, BlockStateProvider> mapEntry : BLOCK_STATE_PROVIDERS.entrySet()) {
//			Class<? extends Block> clazz = mapEntry.getKey();
//			BlockStateProvider provider = mapEntry.getValue();
//			if (clazz.isInstance(block)) {
//				provider.generate(generator, block, id);
//				break;
//			}
//		}
//
////		if (block.getClass() == Block.class) {
////			generator.registerSimpleCubeAll(block);
////			generator.registerParentedItemModel(block, id);
////		}
//	}
//
//	@Override
//	public void generateItemModels(ItemModelGenerator generator) {
//		Registries.ITEM.streamEntries()
//			.filter(AristotlesComedyDataGenerator::ownsRegistry)
//			.forEach(entry -> this.generateItemModel(generator, entry));
//	}
//
//	public void generateItemModel(ItemModelGenerator generator, RegistryEntry<Item> entry) {
//		Item item = entry.value();
//
//		if (item instanceof ToolItem) {
//			generator.register(item, Models.HANDHELD);
//		} else if (item.getClass() == Item.class) {
//			generator.register(item, Models.GENERATED);
//		}
//	}

}
