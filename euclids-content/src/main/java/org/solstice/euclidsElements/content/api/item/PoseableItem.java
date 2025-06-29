package org.solstice.euclidsElements.content.api.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;

public interface PoseableItem {

    @Environment(EnvType.CLIENT)
    void poseModel(ModelPart root, LivingEntity entity, ItemStack stack, Arm arm);

	static ModelPart getArmModel(ModelPart root, Arm arm) {
		return switch (arm) {
			case RIGHT -> root.getChild("right_arm");
			case LEFT -> root.getChild("left_arm");
		};
	}

}
