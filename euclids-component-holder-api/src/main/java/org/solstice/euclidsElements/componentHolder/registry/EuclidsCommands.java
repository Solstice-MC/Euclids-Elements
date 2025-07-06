package org.solstice.euclidsElements.componentHolder.registry;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.solstice.euclidsElements.componentHolder.content.command.ComponentCommand;

public class EuclidsCommands {

	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, access, environment) -> {
			ComponentCommand.register(dispatcher, access);
		});
	}

}
