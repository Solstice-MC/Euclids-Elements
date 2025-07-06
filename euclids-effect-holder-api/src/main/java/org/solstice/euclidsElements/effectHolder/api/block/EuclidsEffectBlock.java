package org.solstice.euclidsElements.effectHolder.api.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;

public interface EuclidsEffectBlock {

	default void onStacksDropped(ServerWorld world, LivingEntity entity, BlockPos pos, ItemStack tool, boolean dropExperience) {
//		this.getBlock().onStacksDropped(this.asBlockState(), world, pos, tool, dropExperience);
	}

	default void onStacksDropped(BlockState state, ServerWorld world, LivingEntity entity, BlockPos pos, ItemStack tool, boolean dropExperience) {
	}

	default void dropExperienceWhenMined(ServerWorld world, LivingEntity entity, BlockPos pos, ItemStack tool, IntProvider experience) {
//		int i = EnchantmentHelper.getBlockExperience(world, tool, experience.get(world.getRandom()));
//		if (i > 0) {
//			this.dropExperience(world, pos, i);
//		}
	}

}
