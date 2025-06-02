package org.solstice.euclidsElements.content.api.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface CollapsableItem {

	void onCollapse(World world, PlayerEntity user);

}
