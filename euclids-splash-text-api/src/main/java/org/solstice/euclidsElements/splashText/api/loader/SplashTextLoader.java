package org.solstice.euclidsElements.splashText.api.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import org.solstice.euclidsElements.splashText.api.manager.SplashTextManager;
import org.solstice.euclidsElements.splashText.api.type.SimpleSplashText;
import org.solstice.euclidsElements.splashText.api.type.SplashText;
import org.solstice.euclidsElements.splashText.api.type.SplashTextFile;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.solstice.euclidsElements.EuclidsElements;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class SplashTextLoader implements SimpleResourceReloadListener<Void> {

	private static final String NEW_SPLASHES_FILE = "splashes.json";
	private static final String OLD_SPLASHES_FILE = "splashes.txt";

    @Override
    public Identifier getFabricId() {
        return EuclidsElements.of("splashes");
    }

	@Override
	public CompletableFuture<Void> apply(Void data, ResourceManager manager, Profiler profiler, Executor executor) {
		return CompletableFuture.runAsync( () -> {}, executor );
	}

	@Override
	public CompletableFuture<Void> load(ResourceManager manager, Profiler profiler, Executor executor) {
		return CompletableFuture.runAsync( () -> load(manager), executor );
	}

    public void load(ResourceManager manager) {
		Map<Identifier, Resource> splashes = manager.findResources("texts", path -> path.getPath().replaceFirst("texts/", "").equals(NEW_SPLASHES_FILE));
		processResource(splashes, JsonParser::parseReader, this::addSplashes);
		Map<Identifier, Resource> oldSplashes = manager.findResources("texts", path -> path.getPath().replaceFirst("texts/", "").equals(OLD_SPLASHES_FILE));
		processResource(oldSplashes, BufferedReader::lines, this::addOldSplashes);
    }

	public static <T> void processResource(Map<Identifier, Resource> resources, Function<BufferedReader, T> transformer, BiConsumer<Identifier, T> loader) {
		resources.forEach((path, resource) -> {
			try {
				T value = transformer.apply(resource.getReader());
				loader.accept(path, value);
			} catch (IOException exception) {
				EuclidsElements.LOGGER.error("Error loading file '{}'", path, exception);
			}
		});
	}

    public void addSplashes(Identifier identifier, JsonElement element) {
		try {
			SplashTextFile.CODEC.parse(JsonOps.INSTANCE, element.getAsJsonObject()).getOrThrow().getTexts()
				.filter(SplashText::validate)
				.forEach(SplashTextManager.INSTANCE::addSplashText);
		} catch (Exception exception) {
			EuclidsElements.LOGGER.error("Unable to load splash file: '{}'", identifier, exception);
		}
    }

    public void addOldSplashes(Identifier ignored, Stream<String> stream) {
        stream.map(SimpleSplashText::new)
			.forEach(SplashTextManager.INSTANCE::addSplashText);
    }

}
