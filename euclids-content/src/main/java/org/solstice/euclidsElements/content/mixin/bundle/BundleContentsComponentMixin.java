package org.solstice.euclidsElements.content.mixin.bundle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BundleContentsComponent.class)
public abstract class BundleContentsComponentMixin {

	@Shadow @Final @Mutable public static Codec<BundleContentsComponent> CODEC;
	@Shadow @Final @Mutable public static PacketCodec<RegistryByteBuf, BundleContentsComponent> PACKET_CODEC;

	@Shadow @Final List<ItemStack> stacks;

	@Unique
	private static List<ItemStack> getStacks(BundleContentsComponent component) {
		return ((BundleContentsComponentMixin)(Object)component).stacks;
	}

	@Unique private static final Codec<Fraction> FRACTION_CODEC = Codec.STRING.xmap(
		Fraction::getFraction,
		Fraction::toProperString
	);

	@Unique private static final PacketCodec<RegistryByteBuf, Fraction> FRACTION_PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.STRING,
		Fraction::toProperString,
		Fraction::getFraction
	);

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void injectCodec(CallbackInfo ci) {
		CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ItemStack.CODEC.listOf().fieldOf("stacks").forGetter(BundleContentsComponentMixin::getStacks),
			FRACTION_CODEC.fieldOf("occupancy").forGetter(BundleContentsComponent::getOccupancy)
		).apply(instance, BundleContentsComponent::new));

		PACKET_CODEC = PacketCodec.tuple(
			ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()),
			BundleContentsComponentMixin::getStacks,
			FRACTION_PACKET_CODEC,
			BundleContentsComponent::getOccupancy,
			BundleContentsComponent::new
		);
	}

}
