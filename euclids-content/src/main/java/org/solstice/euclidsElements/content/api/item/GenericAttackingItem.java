package org.solstice.euclidsElements.content.api.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface GenericAttackingItem {

	void genericAttack(World world, PlayerEntity player, ItemStack stack);

}
