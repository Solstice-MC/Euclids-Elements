package org.solstice.euclidsElements.construct.api.dispenser;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.BlockPlacementDispenserBehavior;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.solstice.euclidsElements.construct.EuclidsConstructAPI;

public class ConstructBlockPlacementDispenserBehavior extends BlockPlacementDispenserBehavior {

	@Override
	protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
		this.setSuccess(false);
		World world = pointer.world();
		if (world == null) return stack;

		Direction dir = pointer.state().get(DispenserBlock.FACING);
		BlockPos pos = pointer.pos().offset(dir);
		Direction trueDir = world.isAir(pos.down()) ? dir : Direction.UP;
		Item item = stack.getItem();
		if (!(item instanceof BlockItem blockItem)) return stack;

		world.setBlockState(pos, blockItem.getBlock().getDefaultState());
		if (!EuclidsConstructAPI.canDispense(world, pos)) {
			world.removeBlock(pos, false);
			return stack;
		}
		world.removeBlock(pos, false);

		var context = new AutomaticItemPlacementContext(pointer.world(), pos, dir, stack, trueDir);
		this.setSuccess(blockItem.place(context).isAccepted());
		EuclidsConstructAPI.trySpawnConstructs(world, pos);

		return stack;
	}

}
