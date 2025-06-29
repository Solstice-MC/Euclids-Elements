package org.solstice.euclidsElements.tag;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.tag.api.MapTagKey;
import org.solstice.euclidsElements.tag.api.MapTagsUpdatedCallback;
import org.solstice.euclidsElements.tag.content.mapTag.MapTagManager;
import org.solstice.euclidsElements.tag.content.network.packets.KnownRegistryMapTagsPacket;
import org.solstice.euclidsElements.tag.content.network.packets.KnownRegistryMapTagsReplyPacket;
import org.solstice.euclidsElements.tag.content.network.packets.RegistryMapTagSyncPacket;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EuclidsTagClientAPI implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientConfigurationNetworking.registerGlobalReceiver(KnownRegistryMapTagsPacket.ID, EuclidsTagClientAPI::handleKnownMapTags);
		ClientPlayNetworking.registerGlobalReceiver(RegistryMapTagSyncPacket.ID, EuclidsTagClientAPI::handleMapTagSync);
	}

	@Environment(EnvType.CLIENT)
	public static void handleKnownMapTags(KnownRegistryMapTagsPacket p, ClientConfigurationNetworking.Context context) {
		Map<RegistryKey<? extends Registry<?>>, Collection<Identifier>> result = new HashMap<>();
		MapTagManager.getMapTags().forEach((key, vals) ->
			result.put(key, vals.keySet())
		);

		context.responseSender().sendPacket(new KnownRegistryMapTagsReplyPacket(result));
	}

	@Environment(EnvType.CLIENT)
	public static <R> void handleMapTagSync(final RegistryMapTagSyncPacket<R> payload, final ClientPlayNetworking.Context context) {
		context.client().submit(() -> {
			try {
				World world = context.client().world;
				if (world == null) return;

				DynamicRegistryManager registryManager = world.getRegistryManager();
				SimpleRegistry<R> registry = (SimpleRegistry<R>) registryManager.get(payload.registryKey());
				registry.getMapTags().clear();
				payload.mapTags().forEach((id, maps) -> {
					MapTagKey<R, ?> mapTag = MapTagManager.getMapTag(payload.registryKey(), id);
					Map<RegistryKey<R>, ?> values = Collections.unmodifiableMap(maps);
					registry.getMapTags().put(mapTag, values);
				});
				MapTagsUpdatedCallback.EVENT.invoker().onMapTagsUpdated(registryManager, registry, MapTagsUpdatedCallback.Cause.CLIENT_SYNC);
			} catch (Exception exception) {
				EuclidsElements.LOGGER.error("Failed to handle registry map tag sync: ", exception);
				context.responseSender().disconnect(Text.literal("Failed to handle registry map tag sync"));
			}
		});
	}

}
