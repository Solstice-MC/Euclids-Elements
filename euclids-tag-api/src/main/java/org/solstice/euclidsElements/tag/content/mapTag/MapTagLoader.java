package org.solstice.euclidsElements.tag.content.mapTag;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.tag.api.MapTagKey;
import org.solstice.euclidsElements.tag.api.MapTagsUpdatedCallback;

import java.io.Reader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class MapTagLoader<T, R> implements ResourceReloader, IdentifiableResourceReloadListener {

	public static final Identifier ID = EuclidsElements.of("map_tags");
	public static final String PATH = "map_tags";

	@Override
	public Identifier getFabricId() {
		return ID;
	}

	private final DynamicRegistryManager registryManager;

	private final Map<RegistryKey<? extends Registry<T>>, LoadResult<T, R>> results = new HashMap<>();

	public MapTagLoader(DynamicRegistryManager registryManager) {
		this.registryManager = registryManager;
	}

	public static String getDirectory(Identifier id) {
		String location = id.getNamespace().equals(Identifier.DEFAULT_NAMESPACE) ? "" : id.getNamespace() + "/";
		return location + id.getPath();
	}

	@Override
	public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
		return this.prepareReload(manager, prepareExecutor)
			.thenCompose(synchronizer::whenPrepared)
			.thenAcceptAsync(this.results::putAll, applyExecutor);
	}

	private CompletableFuture<Map<RegistryKey<? extends Registry<T>>, LoadResult<T,  R>>> prepareReload(ResourceManager manager, Executor executor) {
		return CompletableFuture.supplyAsync(() -> prepareReload(manager, this.registryManager), executor);
	}

	@SuppressWarnings("unchecked")
	private static <T, R> Map<RegistryKey<? extends Registry<T>>, LoadResult<T, R>> prepareReload(ResourceManager manager, DynamicRegistryManager registryManager) {
		RegistryOps<JsonElement> ops = RegistryOps.of(JsonOps.INSTANCE, registryManager);
		Map<RegistryKey<? extends Registry<T>>, LoadResult<T, R>> values = new HashMap<>();

		registryManager.streamAllRegistries().forEach( registryEntry -> {
			RegistryKey<Registry<T>> registryKey = (RegistryKey<Registry<T>>) registryEntry.key();

			ResourceFinder finder = ResourceFinder.json(PATH + "/" + getDirectory(registryKey.getValue()));
			finder.findAllResources(manager).forEach((key, resources) -> {
				Identifier attachmentId = finder.toResourceId(key);
				MapTagKey<T, R> attachment = (MapTagKey<T, R>) MapTagManager.getMapTag(registryKey, attachmentId);
				if (attachment == null) {
					EuclidsElements.LOGGER.warn("Found map tag file for non-existent map tag type '{}' on registry '{}'",
						attachmentId, registryKey.getValue()
					);
					return;
				}

				LoadResult<T, R> result = values.computeIfAbsent(
					registryKey,
					k -> new LoadResult<>(new HashMap<>())
				);
				result.contents.put(attachment, readData(ops, attachment, registryKey, resources));

			});

		});

		return values;
	}

	public void apply() {
		this.results.forEach((key, result) ->
			this.apply((SimpleRegistry<T>) registryManager.get(key), result)
		);
		this.results.clear();
	}

	private void apply(SimpleRegistry<T> registry, LoadResult<T, R> result) {
		registry.getMapTags().clear();

		result.contents.forEach((key, entries) ->
			registry.getMapTags().put(key, this.buildMapTag(registry, entries))
		);

		MapTagsUpdatedCallback.EVENT.invoker().onMapTagsUpdated(registryManager, registry, MapTagsUpdatedCallback.Cause.SERVER_RELOAD);
	}
	private static <T, R> List<MapTagData<R>> readData(
		RegistryOps<JsonElement> ops,
		MapTagKey<T, R> attachmentType,
		RegistryKey<Registry<T>> registryKey,
		List<Resource> resources
	) {
		Codec<MapTagData<R>> codec = MapTagData.codec(attachmentType);
		List<MapTagData<R>> entries = new LinkedList<>();
		for (Resource resource : resources) {
			try (Reader reader = resource.getReader()) {
				JsonElement element = JsonParser.parseReader(reader);
				MapTagData<R> data = codec.decode(ops, element).getOrThrow().getFirst();
				entries.add(data);
			} catch (Exception exception) {
				EuclidsElements.LOGGER.error("Could not read map tag of type {} for registry {}",
					attachmentType.getId(), registryKey, exception
				);
			}
		}
		return entries;
	}

	private Map<RegistryKey<T>, R> buildMapTag(
		Registry<T> registry,
		List<MapTagData<R>> entries
	) {
		Map<RegistryKey<T>, R> result = new HashMap<>();

		entries.forEach(data -> {
			if (data.replace()) result.clear();

			data.entries().forEach((tagEntry, value) -> {
				tagEntry.getEntries(registry).stream()
					.map(RegistryEntry::getKey)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.forEach(key -> result.put(key, value));
			});

			data.removals().forEach(tagEntry -> {
				tagEntry.getEntries(registry).stream()
					.map(RegistryEntry::getKey)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.forEach(result::remove);
			});
		});

		return result;
	}

	public record LoadResult<T, R>(Map<MapTagKey<T, R>, List<MapTagData<R>>> contents) {}

}
