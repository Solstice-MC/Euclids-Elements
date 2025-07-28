package org.solstice.euclidsElements.componentHolder.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.componentHolder.api.SerializableComponentHolder;
import org.solstice.euclidsElements.componentHolder.registry.EuclidsTrackedDataHandlers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements SerializableComponentHolder {

	@Unique
	private static final String COMPONENT_ID = EuclidsElements.of("components").toString();

	@Unique
	private static final Codec<ComponentMap> COMPONENT_CODEC = ComponentMap.CODEC.optionalFieldOf("components", ComponentMap.EMPTY).codec();

	@Unique
	private static final TrackedData<ComponentMapImpl> TRACKED_COMPONENTS = DataTracker.registerData(Entity.class, EuclidsTrackedDataHandlers.COMPONENT_MAP);

	@Shadow @Final protected DataTracker dataTracker;

	@Shadow
	public abstract World getWorld();

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;initDataTracker(Lnet/minecraft/entity/data/DataTracker$Builder;)V"))
	private void addDataTrackers(CallbackInfo ci, @Local DataTracker.Builder builder) {
		builder.add(TRACKED_COMPONENTS, new ComponentMapImpl(ComponentMap.EMPTY));
	}

	@Override
	public ComponentMapImpl getMutableComponents() {
		return this.dataTracker.get(TRACKED_COMPONENTS);
	}

	@Override
	public void setComponents(ComponentMapImpl components) {
		this.dataTracker.set(TRACKED_COMPONENTS, components);
	}

	@Inject(method = "writeNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
	private void writeComponents(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
		this.writeComponentsToNbt(nbt, this.getWorld().getRegistryManager());
	}

	@Inject(method = "readNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
	private void readComponents(NbtCompound nbt, CallbackInfo ci) {
		this.readComponentsFromNbt(nbt, this.getWorld().getRegistryManager());
	}

}
