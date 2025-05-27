package org.solstice.euclidsElements.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;

import java.util.Arrays;

public class EuclidsServerEvents {

	public static final Event<AfterResourceReload> AFTER_RESOURCES_LOADED = EventFactory.createArrayBacked(AfterResourceReload.class,
		callbacks -> server -> Arrays.stream(callbacks).forEach(callback -> callback.afterResourcesReloaded(server))
	);

	public static final Event<BeforeStart> BEFORE_START = EventFactory.createArrayBacked(BeforeStart.class,
		callbacks -> server -> Arrays.stream(callbacks).forEach(callback -> callback.beforeStart(server))
	);

	@FunctionalInterface
	public interface AfterResourceReload {
		void afterResourcesReloaded(MinecraftServer server);
	}

	@FunctionalInterface
	public interface BeforeStart {
		void beforeStart(MinecraftServer server);
	}

}
