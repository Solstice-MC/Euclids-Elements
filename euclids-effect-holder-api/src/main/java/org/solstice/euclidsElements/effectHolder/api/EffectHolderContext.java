package org.solstice.euclidsElements.effectHolder.api;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public record EffectHolderContext (
	@Nullable World world,
	@Nullable LivingEntity entity,
	ItemStack stack,
	@Nullable EquipmentSlot slot
) {}
