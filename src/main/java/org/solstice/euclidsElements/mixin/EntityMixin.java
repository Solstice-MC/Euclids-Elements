package org.solstice.euclidsElements.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.api.componentHolder.AdvancedComponentHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements AdvancedComponentHolder {

	@Unique
	private static final Codec<ComponentMap> COMPONENT_CODEC = ComponentMap.CODEC.optionalFieldOf("components", ComponentMap.EMPTY).codec();

	@Unique
	private ComponentMapImpl components = new ComponentMapImpl(ComponentMap.EMPTY);

	@Override
	public ComponentMapImpl getComponents() {
		return this.components;
	}

	@Inject(method = "writeNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
	private void writeComponents(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
		COMPONENT_CODEC.encodeStart(NbtOps.INSTANCE, this.components)
			.resultOrPartial(error -> EuclidsElements.LOGGER.warn("Failed to save entity components: {}", error))
			.ifPresent(newNbt -> nbt.copyFrom((NbtCompound)newNbt));
	}

	@Inject(method = "readNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
	private void readComponents(NbtCompound nbt, CallbackInfo ci) {
		COMPONENT_CODEC.parse(NbtOps.INSTANCE, nbt)
			.resultOrPartial(error -> EuclidsElements.LOGGER.warn("Failed to load entity components: {}", error))
			.ifPresent(components -> this.components = new ComponentMapImpl(components));
	}

}
