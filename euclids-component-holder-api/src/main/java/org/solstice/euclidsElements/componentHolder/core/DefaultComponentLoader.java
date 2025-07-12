package org.solstice.euclidsElements.componentHolder.core;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.componentHolder.api.ModifiableComponentHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultComponentLoader implements SimpleSynchronousResourceReloadListener {

	public static final String PATH = "default_components";
	public static final Identifier ID = EuclidsElements.of(PATH);

	@Override
	public Identifier getFabricId() {
		return ID;
	}

	protected final RegistryWrapper.WrapperLookup lookup;

	protected final Map<RegistryKey<? extends Registry<?>>, List<ComponentChangeData>> loadedComponentChanges = new HashMap<>();

	public DefaultComponentLoader(RegistryWrapper.WrapperLookup lookup) {
		this.lookup = lookup;
	}

	public static String getDirectory(Identifier id) {
		String location = id.getNamespace().equals(Identifier.DEFAULT_NAMESPACE) ? "" : id.getNamespace() + "/";
		return location + id.getPath();
	}

	@Override
	public void reload(ResourceManager manager) {
		lookup.streamAllRegistryKeys().forEach( (registryReference) -> {
			// WIP monkey patch to only apply component changes to items for now
			if (!registryReference.equals(RegistryKeys.ITEM)) return;

			ResourceFinder finder = ResourceFinder.json(PATH + "/" + getDirectory(registryReference.getValue()));
			finder.findResources(manager).forEach((key, resource) -> {
				List<ComponentChangeData> loadedComponentChanges = this.loadedComponentChanges.computeIfAbsent(
					registryReference,
					k -> new ArrayList<>()
				);

				ComponentChangeData data = ComponentChangeData.fromResource(resource);
				loadedComponentChanges.add(data);
			});
		});
	}

	public void apply(DynamicRegistryManager registryManager, boolean client) {
		if (client) return;

		this.loadedComponentChanges.forEach((registryReference, loadedMapTag) ->
			this.applyChanges(registryManager, registryReference, loadedMapTag)
		);
		this.loadedComponentChanges.clear();
	}

	private void applyChanges(DynamicRegistryManager registryManager, RegistryKey<? extends Registry<?>> registryReference, List<ComponentChangeData> data) {
		Registry<?> registry = registryManager.get(registryReference);

		data.forEach(componentChanges -> {
			RegistryEntryList<?> entries = componentChanges.target().getEntries(registry);
			this.modifyEntries(entries, componentChanges);
		});
	}

	public void modifyEntries(RegistryEntryList<?> entries, ComponentChangeData data) {
		List<ModifiableComponentHolder> holders = entries.stream()
			.map(RegistryEntry::value)
			.filter(value -> value instanceof ModifiableComponentHolder)
			.map(value -> (ModifiableComponentHolder) value)
			.toList();
		for (ModifiableComponentHolder holder : holders) {
			ComponentMap.Builder builder = ComponentMap.builder().addAll(holder.getComponents());
			builder.addAll(data.components());
			ComponentMapImpl components = new ComponentMapImpl(builder.build());
			holder.setComponents(components);
		}
	}


}
