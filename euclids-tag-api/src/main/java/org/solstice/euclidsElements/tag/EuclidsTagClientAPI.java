package org.solstice.euclidsElements.tag;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.solstice.euclidsElements.tag.content.network.packets.KnownRegistryMapTagsPacket;
import org.solstice.euclidsElements.tag.content.network.packets.RegistryMapTagSyncPacket;

public class EuclidsTagClientAPI implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientConfigurationNetworking.registerGlobalReceiver(KnownRegistryMapTagsPacket.ID, KnownRegistryMapTagsPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(RegistryMapTagSyncPacket.ID, RegistryMapTagSyncPacket::handle);
	}

}
