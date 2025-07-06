package org.solstice.euclidsElements.effectHolder.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;


@Mixin(Block.class)
public abstract class BlockMixin {


	@Shadow public static void dropStack(World world, BlockPos pos, ItemStack stack) {}

	@Shadow public static List<ItemStack> getDroppedStacks(BlockState state, ServerWorld world, BlockPos pos, @Nullable BlockEntity blockEntity, Entity entity, ItemStack tool) {
		return List.of();
	}

	@WrapMethod(method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V")
	private static void dropStacks(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack tool, Operation<Void> original) {
		if (world instanceof ServerWorld) {
			getDroppedStacks(state, (ServerWorld)world, pos, blockEntity, entity, tool).forEach(stack -> dropStack(world, pos, stack));
			state.onStacksDropped((ServerWorld)world, pos, tool, true);
		}
	}

}
