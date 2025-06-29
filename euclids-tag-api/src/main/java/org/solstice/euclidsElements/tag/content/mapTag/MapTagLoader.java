package org.solstice.euclidsElements.tag.content.mapTag;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.tag.api.MapTagKey;
import org.solstice.euclidsElements.tag.api.MapTagsUpdatedCallback;

import java.io.Reader;
import java.util.*;

public class MapTagLoader<T, R> implements SimpleSynchronousResourceReloadListener {

	public static final Identifier ID = EuclidsElements.of("map_tags");
	public static final String PATH = "map_tags";

	@Override
	public Identifier getFabricId() {
		return ID;
	}

	private final RegistryWrapper.WrapperLookup registryLookup;
	private final Map<RegistryKey<? extends Registry<T>>, LoadedMapTag<T, R>> loadedMapTags = new HashMap<>();

	public MapTagLoader(RegistryWrapper.WrapperLookup registryLookup) {
		this.registryLookup = registryLookup;
	}

	public static String getDirectory(Identifier id) {
		String location = id.getNamespace().equals(Identifier.DEFAULT_NAMESPACE) ? "" : id.getNamespace() + "/";
		return location + id.getPath();
	}

	@Override
	public void reload(ResourceManager resourceManager) {
		this.loadedMapTags.putAll(this.loadMapTags(resourceManager, this.registryLookup));
	}

	@SuppressWarnings("unchecked")
	private Map<RegistryKey<? extends Registry<T>>, LoadedMapTag<T, R>> loadMapTags(ResourceManager manager, RegistryWrapper.WrapperLookup registryLookup) {
		RegistryOps<JsonElement> ops = RegistryOps.of(JsonOps.INSTANCE, registryLookup);
		Map<RegistryKey<? extends Registry<T>>, LoadedMapTag<T, R>> result = new HashMap<>();

		registryLookup.streamAllRegistryKeys().forEach( rawReference -> {
			RegistryKey<Registry<T>> registryReference = (RegistryKey<Registry<T>>) rawReference;
			ResourceFinder finder = ResourceFinder.json(PATH + "/" + getDirectory(registryReference.getValue()));
			finder.findAllResources(manager).forEach((key, resources) -> {
				Identifier id = finder.toResourceId(key);
				MapTagKey<T, R> mapTag = MapTagManager.getMapTag(registryReference, id);
				if (mapTag == null) {
					EuclidsElements.LOGGER.warn("Found map tag file for non-existent map tag type '{}' on registry '{}'",
						id, registryReference.getValue()
					);
					return;
				}

				LoadedMapTag<T, R> loadedMapTag = result.computeIfAbsent(
					registryReference,
					k -> new LoadedMapTag<>(new HashMap<>())
				);
				loadedMapTag.files.put(mapTag, this.readFromFile(ops, mapTag, registryReference, resources));

			});
		});

		return result;
	}

	private List<MapTagFile<R>> readFromFile(
		RegistryOps<JsonElement> ops,
		MapTagKey<T, R> mapTag,
		RegistryKey<Registry<T>> registryReference,
		List<Resource> resources
	) {
		Codec<MapTagFile<R>> codec = MapTagFile.codec(mapTag);
		List<MapTagFile<R>> entries = new LinkedList<>();
		for (Resource resource : resources) {
			try (Reader reader = resource.getReader()) {
				JsonElement element = JsonParser.parseReader(reader);
				MapTagFile<R> data = codec.decode(ops, element).getOrThrow().getFirst();
				entries.add(data);
			} catch (Exception exception) {
				EuclidsElements.LOGGER.error("Could not read map tag of type {} for registry {}",
					mapTag.getId(), registryReference, exception
				);
			}
		}
		return entries;
	}

	public record LoadedMapTag<T, R>(Map<MapTagKey<T, R>, List<MapTagFile<R>>> files) {}

	public void apply(DynamicRegistryManager registryManager, boolean client) {
		if (client) return;
		this.loadedMapTags.forEach((registryReference, loadedMapTag) ->
			this.applyMapTags(registryManager, registryReference, loadedMapTag)
		);
		this.loadedMapTags.clear();
	}

	private void applyMapTags(DynamicRegistryManager registryManager, RegistryKey<? extends Registry<T>> registryReference, LoadedMapTag<T, R> loadedMapTag) {
		Registry<T> registry = registryManager.get(registryReference);
		registry.getMapTags().clear();

		loadedMapTag.files.forEach((MapTagKey<T, R> mapTag, List<MapTagFile<R>> entries) ->
			registry.getMapTags().put(mapTag, this.getMapTagData(registry, entries))
		);

		MapTagsUpdatedCallback.EVENT.invoker().onMapTagsUpdated(registryManager, registry, MapTagsUpdatedCallback.Cause.SERVER_RELOAD);
	}

	private Map<RegistryKey<T>, R> getMapTagData(
		Registry<T> registry,
		List<MapTagFile<R>> entries
	) {
		Map<RegistryKey<T>, R> result = new HashMap<>();

		entries.forEach(data -> {
			if (data.replace()) result.clear();

			data.entries().forEach((tagEntry, value) -> tagEntry.getEntries(registry).stream()
				.map(RegistryEntry::getKey)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.forEach(key -> result.put(key, value)));

			data.removals().forEach(tagEntry -> tagEntry.getEntries(registry).stream()
				.map(RegistryEntry::getKey)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.forEach(result::remove));
		});

		return result;
	}

}
