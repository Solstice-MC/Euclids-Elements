package org.solstice.euclidsElements.content.api.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface InteractionPreventingItem {

	boolean doBlockInteractions(LivingEntity user, BlockState state, Direction direction);

	static boolean itemBasedInteractions(LivingEntity user, Hand hand, BlockHitResult hit) {
		ItemStack stack = user.getStackInHand(hand);
		if (stack.getItem() instanceof InteractionPreventingItem item) {
			World world = user.getEntityWorld();
			BlockState state = world.getBlockState(hit.getBlockPos());
			Direction side = hit.getSide();
			return item.doBlockInteractions(user, state, side);
		}
		return false;
	}

}
