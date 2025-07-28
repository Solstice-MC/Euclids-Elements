package org.solstice.euclidsElements.componentHolder;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.solstice.euclidsElements.EuclidsElements;

import java.util.HashMap;
import java.util.Map;

public class CustomComponentsState extends PersistentState {

	private static final PersistentState.Type<CustomComponentsState> TYPE =
		new PersistentState.Type<>(CustomComponentsState::createNew, CustomComponentsState::createFromNbt, null);

	private static final String COMPONENT_ID = EuclidsElements.of("components").toString();
	private static final Codec<ComponentMap> COMPONENT_CODEC = ComponentMap.CODEC.optionalFieldOf("components", ComponentMap.EMPTY).codec();

	protected ComponentMapImpl serverComponents = new ComponentMapImpl(ComponentMap.EMPTY);
	protected Map<RegistryKey<World>, ComponentMapImpl> worldComponents = new HashMap<>();

	@Nullable
	public static CustomComponentsState get(MinecraftServer server) {
		ServerWorld world = server.getOverworld();
		if (world == null) return null;

		CustomComponentsState state = world.getPersistentStateManager().getOrCreate(TYPE, EuclidsElements.of("components").toString());
		if (state == null) return null;

		state.markDirty();
		return state;
	}

	public ComponentMapImpl getComponents(World world) {
		return this.worldComponents.getOrDefault(world.getRegistryKey(), new ComponentMapImpl(ComponentMap.EMPTY));
	}

	public void setComponents(World world, ComponentMapImpl components) {
		this.worldComponents.put(world.getRegistryKey(), components);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		NbtCompound worldComponents = new NbtCompound();

		RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, registryLookup);
		this.worldComponents.forEach((worldKey, components) -> {
			NbtCompound componentsCompound = new NbtCompound();
			COMPONENT_CODEC.encodeStart(ops, components)
				.resultOrPartial(error -> EuclidsElements.LOGGER.warn("Failed to save entity components: {}", error))
				.ifPresent(newNbt -> componentsCompound.put(COMPONENT_ID, newNbt));
			worldComponents.put(worldKey.getValue().toString(), componentsCompound);
		});

		nbt.put("world_components", worldComponents);
		return nbt;
	}

	public static CustomComponentsState createNew() {
		CustomComponentsState state = new CustomComponentsState();
		state.worldComponents = new HashMap<>();
		return state;
	}

	public static CustomComponentsState createFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		CustomComponentsState state = new CustomComponentsState();
		NbtCompound worldComponents = nbt.getCompound("world_components");

		RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, registryLookup);
		worldComponents.getCompound("world_components").getKeys().forEach(rawKey -> {
			RegistryKey<World> key = RegistryKey.of(RegistryKeys.WORLD, Identifier.tryParse(rawKey));
			NbtCompound components = worldComponents.getCompound(rawKey);

			COMPONENT_CODEC.parse(ops, components)
				.resultOrPartial(error -> EuclidsElements.LOGGER.warn("Failed to load entity components: {}", error))
				.ifPresent(componentMap -> state.worldComponents.put(key, new ComponentMapImpl(componentMap)));
		});

		return state;
	}

}
