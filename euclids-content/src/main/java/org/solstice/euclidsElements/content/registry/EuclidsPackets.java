package org.solstice.euclidsElements.content.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.solstice.euclidsElements.content.api.packet.GenericAttackPacket;

public class EuclidsPackets {

	public static void init() {
		PayloadTypeRegistry.playC2S().register(GenericAttackPacket.ID, GenericAttackPacket.PACKET_CODEC);
	}

	@Environment(EnvType.CLIENT)
	public static void clientInit() {
		ServerPlayNetworking.registerGlobalReceiver(GenericAttackPacket.ID, GenericAttackPacket::openResearchScreen);
	}

}
