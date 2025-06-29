package org.solstice.euclidsElements.content;

import net.fabricmc.api.ClientModInitializer;
import org.solstice.euclidsElements.content.registry.EuclidsPackets;

public class EuclidsContentClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EuclidsPackets.clientInit();
	}

}
