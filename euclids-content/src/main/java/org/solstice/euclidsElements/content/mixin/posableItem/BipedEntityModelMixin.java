package org.solstice.euclidsElements.content.mixin.posableItem;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import org.solstice.euclidsElements.content.api.item.PoseableItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AnimalModel<T> implements ModelWithArms, ModelWithHead {

	@Shadow protected abstract Arm getPreferredArm(T entity);

	@Unique public ModelPart root;

	@Inject(method = "<init>(Lnet/minecraft/client/model/ModelPart;Ljava/util/function/Function;)V", at = @At("TAIL"))
	private void getRoot(CallbackInfo ci, @Local(argsOnly = true) ModelPart root) {
		this.root = root;
	}

	@Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
	void jolted$positionRightArm(T entity, float f, float g, float h, float i, float j, CallbackInfo ci) {
		ItemStack mainStack = entity.getMainHandStack();
		ItemStack offStack = entity.getOffHandStack();
		Arm arm = Arm.values()[this.getPreferredArm(entity).ordinal()];
		if (mainStack.getItem() instanceof PoseableItem poseable) poseable.poseModel(this.root, entity, mainStack, arm);
		if (offStack.getItem() instanceof PoseableItem poseable) poseable.poseModel(this.root, entity, offStack, arm.getOpposite());
	}

}
