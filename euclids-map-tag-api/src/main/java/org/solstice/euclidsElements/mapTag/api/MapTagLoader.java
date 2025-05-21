package org.solstice.euclidsElements.mapTag.api;

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

import java.io.Reader;
import java.util.*;

public class MapTagLoader<T, R> implements SimpleSynchronousResourceReloadListener {

	public static final Identifier ID = EuclidsElements.of("map_tags");
	public static final String PATH = "map_tags";

	public static String getFolderLocation(Identifier id) {
		String location = id.getNamespace().equals(Identifier.DEFAULT_NAMESPACE) ? "" : id.getNamespace() + "/";
		return location + id.getPath();
	}

	@Override
	public Identifier getFabricId() {
		return ID;
	}

	private final DynamicRegistryManager registryAccess;

	public MapTagLoader(DynamicRegistryManager registryAccess) {
		this.registryAccess = registryAccess;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void reload(ResourceManager manager) {
		RegistryOps<JsonElement> ops = RegistryOps.of(JsonOps.INSTANCE, this.registryAccess);
		Map<RegistryKey<Registry<T>>, Map<MapTagKey<T, R>, List<MapTagFileContent<R>>>> test = new HashMap<>();
		this.registryAccess.streamAllRegistryKeys()
			.map(key -> (RegistryKey<Registry<T>>) key)
			.forEach(key -> test.put(key, test(manager, ops, key)));

		test.forEach((registryReference, test2) -> {
			Registry<T> registry = this.registryAccess.get(registryReference);
			test2.forEach((mapTagKey, content) -> {
				content.forEach(mapTagContent -> {
					mapTagContent.entries().forEach((mapTagEntry, value) -> {
						List<RegistryEntry<T>> entries = mapTagEntry.getEntries(registry);
						entries.forEach(entry -> {
							entry.addValue((MapTagKey<T, Object>) mapTagKey, value);
							System.out.println(entry.getMapTags());
						});
					});
				});
			});
		});
	}

	public static <T, R> Map<MapTagKey<T, R>, List<MapTagFileContent<R>>> test(
		ResourceManager manager,
		RegistryOps<JsonElement> ops,
		RegistryKey<Registry<T>> registryKey
	) {
		Map<MapTagKey<T, R>, List<MapTagFileContent<R>>> results = new HashMap<>();
		ResourceFinder finder = ResourceFinder.json(PATH + "/" + getFolderLocation(registryKey.getValue()));

		finder.findAllResources(manager).forEach((path, resources) -> {
			Identifier id = finder.toResourceId(path);
			MapTagKey<T, R> mapTagKey = MapTagManager.get(registryKey, id);
			if (mapTagKey == null) return;

			List<MapTagFileContent<R>> contents = readContents(ops, mapTagKey, resources);
			results.put(mapTagKey, contents);
		});
		return results;
	}

	@SuppressWarnings("unchecked")
	public static <R> List<MapTagFileContent<R>> readContents(RegistryOps<JsonElement> ops, MapTagKey<?, ?> mapTagKey, List<Resource> resources) {
		List<MapTagFileContent<R>> result = new LinkedList<>();
		resources.forEach(resource -> {
			try (Reader reader = resource.getReader()) {
				JsonElement element = JsonParser.parseReader(reader);
				Codec<MapTagFileContent<R>> codec = MapTagFileContent.codec((MapTagKey<?, R>) mapTagKey);
				result.add(codec.decode(ops, element).getOrThrow().getFirst());
			} catch (Exception exception) {
				EuclidsElements.LOGGER.error("Could not read tag key {}", mapTagKey.getId(), exception);
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

//	public static <R> List<RegistryKey<R>> keysFromEntry(Registry<R> registry, TagEntry entry) {
//		List<RegistryKey<R>> result = new ArrayList<>();
//		if (entry.tag) {
//			TagKey<R> key = TagKey.of(registry, entry.id);
//			registry.iterateEntries()
//		}
//		return result;
//	}

//	public record LoadResult<T, R>(Map<MapTagKey<T, R>, List<MapTagContent<R>>> results) {}

//	public record LoadResult<T, R>(Map<MapTagKey<T, R>, List<MapTagContent<R>>> results) {}

}
