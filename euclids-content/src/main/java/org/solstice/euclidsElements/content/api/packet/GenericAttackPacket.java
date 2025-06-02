package org.solstice.euclidsElements.content.api.packet;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.content.api.item.GenericAttackingItem;

public record GenericAttackPacket(
	ItemStack stack
) implements CustomPayload {

	public static final Codec<GenericAttackPacket> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		ItemStack.CODEC.fieldOf("stack").forGetter(GenericAttackPacket::stack)
	).apply(instance, GenericAttackPacket::new));

	public static final PacketCodec<RegistryByteBuf, GenericAttackPacket> PACKET_CODEC = PacketCodecs.unlimitedRegistryCodec(CODEC);

	public static final Id<GenericAttackPacket> ID = new Id<>(EuclidsElements.of("generic_attack"));

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void openResearchScreen(GenericAttackPacket packet, ServerPlayNetworking.Context context) {
		((GenericAttackingItem)packet.stack.getItem()).genericAttack(context.player().getWorld(), context.player(), packet.stack);
	}

}
