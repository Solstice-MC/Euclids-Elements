package org.solstice.euclidsElements.construct.mixin.entity;

import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.solstice.euclidsElements.construct.api.entity.ConstructableEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(IronGolemEntity.class)
public abstract class IronGolemEntityMixin implements ConstructableEntity {

	@Shadow
	public abstract void setPlayerCreated(boolean playerCreated);

	@Override
	public void onConstructed(BlockPattern.Result result, World world, BlockPos headPos) {
		setPlayerCreated(true);
	}

}
