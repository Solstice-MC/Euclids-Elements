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

}
