package org.solstice.euclidsElements.tag;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.tag.api.MapTagKey;
import org.solstice.euclidsElements.tag.content.mapTag.MapTagLoader;
import org.solstice.euclidsElements.tag.content.mapTag.MapTagManager;
import org.solstice.euclidsElements.tag.content.network.packets.KnownRegistryMapTagsPacket;
import org.solstice.euclidsElements.tag.content.network.packets.KnownRegistryMapTagsReplyPacket;
import org.solstice.euclidsElements.tag.content.network.packets.RegistryMapTagSyncPacket;
import org.solstice.euclidsElements.tag.content.network.task.RegistryMapTagTask;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class EuclidsTagAPI implements ModInitializer {

	@Override
	public void onInitialize() {
		AtomicReference<MapTagLoader<?, ?>> loaderReference = new AtomicReference<>();
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(MapTagLoader.ID, registries -> {
			MapTagLoader<?, ?> loader = new MapTagLoader<>(registries);
			loaderReference.set(loader);
			return loader;
		});
		CommonLifecycleEvents.TAGS_LOADED.register((registries, client) ->
			loaderReference.get().apply(registries, client)
		);

		ServerConfigurationConnectionEvents.CONFIGURE.register(RegistryMapTagTask::register);
		PayloadTypeRegistry.configurationC2S().register(KnownRegistryMapTagsReplyPacket.ID, KnownRegistryMapTagsReplyPacket.PACKET_CODEC);
		PayloadTypeRegistry.configurationS2C().register(KnownRegistryMapTagsPacket.ID, KnownRegistryMapTagsPacket.PACKET_CODEC);
		PayloadTypeRegistry.playS2C().register(RegistryMapTagSyncPacket.ID, RegistryMapTagSyncPacket.PACKET_CODEC);
		ServerConfigurationNetworking.registerGlobalReceiver(KnownRegistryMapTagsReplyPacket.ID, KnownRegistryMapTagsReplyPacket::handle);

		ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(EuclidsTagAPI::syncMapTags);
	}

	public static void syncMapTags(ServerPlayerEntity player, boolean joined) {
		MinecraftServer server = player.getServer();
		if (server == null) return;
		DynamicRegistryManager.Immutable registryManager = server.getRegistryManager();

		MapTagManager.getMapTags().forEach((registryReference, values) -> {
			Optional<Registry<Object>> registry = registryManager.getOptional(registryReference);

			if (registry.isEmpty()) return;
			if (!ServerPlayNetworking.canSend(player, RegistryMapTagSyncPacket.ID)) return;
			if (player.networkHandler.connection.isLocal()) return;

			Map<RegistryKey<? extends Registry<?>>, Collection<Identifier>> playerMaps = player.networkHandler.connection.channel.attr(KnownRegistryMapTagsReplyPacket.KNOWN_MAP_TAGS).get();
			if (playerMaps == null) return;

			handleSync(player, registry.get(), playerMaps.getOrDefault(registryReference, List.of()));
		});
	}

	protected static <T> void handleSync(ServerPlayerEntity player, Registry<T> registry, Collection<Identifier> values) {
		if (values.isEmpty()) return;

		Map<Identifier, Map<RegistryKey<T>, ?>> result = new HashMap<>();
		values.forEach(key -> {
			MapTagKey<T, ?> mapTag = MapTagManager.getMapTag(registry.getKey(), key);
			if (mapTag == null || mapTag.getPacketCodec() == null) return;
			result.put(key, registry.getMapTag(mapTag));
		});

		if (!result.isEmpty()) ServerPlayNetworking.send(player,
			new RegistryMapTagSyncPacket<>(registry.getKey(), result)
		);
	}

}
