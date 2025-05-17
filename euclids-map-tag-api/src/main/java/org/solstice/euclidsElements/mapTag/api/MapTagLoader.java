package org.solstice.euclidsElements.mapTag.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.TagEntry;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.EuclidsElements;

import java.io.Reader;
import java.util.*;

public class MapTagLoader implements SimpleSynchronousResourceReloadListener {

	public static final Identifier ID = EuclidsElements.of("map_tags");
	public static final String PATH = "data_maps";

	public static String getFolderLocation(Identifier id) {
		String location = id.getNamespace().equals(Identifier.DEFAULT_NAMESPACE) ? "" : id.getNamespace() + "/";
		return location + id.getPath();
	}

	@Override
	public Identifier getFabricId() {
		return ID;
	}

	public Map<RegistryKey<? extends Registry<?>>, LoadResult<?, ?>> results;
//	public Map<RegistryKey<? extends Registry<?>>, LoadResult<?, ?>> results;
//	public Map<RegistryKey<? extends Registry<?>>, Map<MapTagKey<?, ?>, List<MapTagFile<?>>>> results;

	private final DynamicRegistryManager registryAccess;

	public MapTagLoader(DynamicRegistryManager registryAccess) {
		this.registryAccess = registryAccess;
	}

	public <T, R> void addResults(RegistryKey<? extends Registry<?>> registryKey, MapTagKey<?, ?> mapTagKey, LoadResult<?, ?> result) {
//		this.results.computeIfAbsent(registryKey,
//			k -> new LoadResult<>(new HashMap<>())
//		).results.put(mapTagKey, result);
	}

	@Override
	public void reload(ResourceManager manager) {
		RegistryOps<JsonElement> ops = RegistryOps.of(JsonOps.INSTANCE, this.registryAccess);
//		this.registryAccess.streamAllRegistryKeys().forEach(key -> this.loadFromKey(manager, ops, key));
//		this.results.forEach((key, results) -> this.apply(key, results));
	}

//	@SuppressWarnings("unchecked")
//	public <T> void apply(RegistryKey<? extends Registry<?>> key, Map<MapTagKey<?, ?>, List<MapTagFile<?>>> results) {
//		Registry<T> registry = (Registry<T>) this.registryAccess.get(key);
//		registry.getMapTags().put(key, buildDataMap(registry, key, results));
//	}

	@SuppressWarnings("unchecked")
	public <T, R> void loadFromKey(ResourceManager manager, RegistryOps<JsonElement> ops, RegistryKey<Registry<T>> registryKey) {
		ResourceFinder finder = ResourceFinder.json(PATH + "/" + getFolderLocation(registryKey.getValue()));

		finder.findAllResources(manager).forEach((path, resources) -> {
			Identifier id = finder.toResourceId(path);
			MapTagKey<T, R> mapTagKey = MapTagManager.get(registryKey, id);
			if (mapTagKey == null) return;

			List<MapTagFile<R>> files = readData(ops, mapTagKey, resources);

			Registry<T> registry = this.registryAccess.get(registryKey);

//			registry.getMapTags().put(registryKey, buildDataMap(registry, mapTagKey, results));



//			LoadResult<T, R> result = new LoadResult<>(mapTagKey, files);
//			this.results.computeIfAbsent((RegistryKey<? extends Registry<?>>) registryKey,
//				k -> new LoadResult<>(new HashMap<>())
//			).results.put(mapTagKey, files);
//			this.results.put((RegistryKey<? extends Registry<?>>) registryKey, result);
		});
	}

	@SuppressWarnings("unchecked")
	public static <R> List<MapTagFile<R>> readData(RegistryOps<JsonElement> ops, MapTagKey<?, ?> mapTagKey, List<Resource> resources) {
		List<MapTagFile<R>> result = new LinkedList<>();
		resources.forEach(resource -> {
			try (Reader reader = resource.getReader()) {
				JsonElement element = JsonParser.parseReader(reader);
				Codec<MapTagFile<R>> codec = MapTagFile.codec((MapTagKey<?, R>) mapTagKey);
				result.add(codec.decode(ops, element).getOrThrow().getFirst());
			} catch (Exception exception) {
				EuclidsElements.LOGGER.error("Could not read tag key {}", mapTagKey.id(), exception);
			}
		});
		return result;
	}

//	private <T, R> Map<RegistryKey<T>, R> buildDataMap(Registry<T> registry, MapTagKey<T, R> mapTagKey, List<MapTagFile<R>> entries) {
//
//		final BiConsumer<Either<TagKey<T>, RegistryKey<Registry<T>>>, Consumer<RegistryEntry<T>>> valueResolver = (key, cons) -> key.ifLeft(
//			tag -> registry.iterateEntries(tag).forEach(cons)).ifRight(
//			k -> cons.accept(registry.entryOf(k)));
//
//		final Map<RegistryKey<Registry<T>>, R> result = new IdentityHashMap<>();
//
//		entries.forEach(file -> {
//
//			file.entries().forEach((tKey, value) -> {
//
//				RegistryKeys.
//
//				valueResolver.accept(tKey, holder -> {
//					RegistryKey<Registry<T>> key = holder.getKey().orElseThrow();
//					final var oldValue = result.get(key);
//					result.put(key, value);
//				});
//			});
//
//
//			file.entries().forEach((key, value) -> {
//
////				registry.iterateEntries()
//				if (value != null) result.put(mapTagKey.registry(), value);
//			});
//
//			file.removals().forEach(entry -> {
//				registry.
////				if (result.containsKey(entry)) result.remove(entry);
//			});
//
//			file.removals().forEach(trRemoval -> valueResolver.accept(trRemoval.key(), holder -> {
//				if (trRemoval.remover().isPresent()) {
//					final var key = holder.unwrapKey().orElseThrow();
//					final var oldValue = result.get(key);
//					if (oldValue != null) {
//						final var newValue = trRemoval.remover().get().remove(oldValue.attachment(), registry,
//							oldValue.source(), holder.value()
//						);
//						if (newValue.isEmpty()) {
//							result.remove(key);
//						} else {
//							result.put(key, new WithSource<>(newValue.get(), oldValue.source()));
//						}
//					}
//				} else {
//					result.remove(holder.unwrapKey().orElseThrow());
//				}
//			}));
//		});
//
//		return result;
//	}

	public static <R> List<RegistryKey<R>> keysFromEntry(Registry<R> registry, TagEntry entry) {
		List<RegistryKey<R>> result = new ArrayList<>();
//		if (entry.tag) {
//			TagKey<R> key = TagKey.of(registry, entry.id);
//			registry.iterateEntries()
//		}
		return result;
	}

	public record LoadResult<T, R>(Map<MapTagKey<T, R>, List<MapTagFile<R>>> results) {}

}
