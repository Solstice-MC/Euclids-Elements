package org.solstice.euclidsElements.construct.api.entity;

import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ConstructableEntity {

	void onConstructed(BlockPattern.Result result, World world, BlockPos headPos);

}
