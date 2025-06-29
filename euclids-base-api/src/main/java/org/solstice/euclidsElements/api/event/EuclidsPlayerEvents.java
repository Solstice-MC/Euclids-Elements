package org.solstice.euclidsElements.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;

import java.util.Arrays;

public class EuclidsPlayerEvents {

	public static final Event<SelectedItemSwitched> SELECTED_ITEM_SWITCHED = EventFactory.createArrayBacked(SelectedItemSwitched.class,
		callbacks -> server -> Arrays.stream(callbacks).forEach(callback -> callback.afterSelectedItemSwitched(server))
	);

	@FunctionalInterface
	public interface SelectedItemSwitched {
		void afterSelectedItemSwitched(MinecraftServer server);
	}


}
