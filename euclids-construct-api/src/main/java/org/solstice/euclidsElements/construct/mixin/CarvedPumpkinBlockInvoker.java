package org.solstice.euclidsElements.construct.mixin;

import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CarvedPumpkinBlock.class)
public interface CarvedPumpkinBlockInvoker {

	@Invoker("spawnEntity")
	static void invokeSpawnEntity(World world, BlockPattern.Result patternResult, Entity entity, BlockPos pos) {}

}
