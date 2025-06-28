package org.solstice.euclidsElements.construct;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.block.*;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.construct.api.dispenser.ConstructBlockPlacementDispenserBehavior;
import org.solstice.euclidsElements.construct.api.type.Construct;

public class EuclidsConstructAPI implements ModInitializer {

	public static final RegistryKey<Registry<Construct>> REGISTRY_KEY = RegistryKey.ofRegistry(EuclidsElements.of("constructs"));

	public static final TagKey<Block> CONSTRUCT_BLOCK = TagKey.of(
		RegistryKeys.BLOCK,
		EuclidsElements.of("construct_blocks")
	);

	@Override
	public void onInitialize() {
		DynamicRegistries.registerSynced(REGISTRY_KEY, Construct.CODEC);
		DispenserBlock.registerBehavior(Blocks.CARVED_PUMPKIN, new ConstructBlockPlacementDispenserBehavior());
		DispenserBlock.registerBehavior(Blocks.WITHER_SKELETON_SKULL, new ConstructBlockPlacementDispenserBehavior());
	}

	public static boolean isConstructBlock(BlockState state) {
		return state.isIn(CONSTRUCT_BLOCK);
	}

	public static boolean isConstructBlock(Block block) {
		return isConstructBlock(block.getDefaultState());
	}

	public static void trySpawnConstructs(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		if (isConstructBlock(block)) world.getRegistryManager().get(REGISTRY_KEY)
			.forEach(construct -> construct.trySpawn(world, pos));
	}

	public static boolean canDispense(WorldView world, BlockPos pos) {
		return world.getRegistryManager().get(REGISTRY_KEY).stream()
			.map(construct -> construct.canConstruct(world, pos))
			.toList().contains(true);
	}

}
