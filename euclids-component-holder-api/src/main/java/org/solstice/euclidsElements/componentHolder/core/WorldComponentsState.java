package org.solstice.euclidsElements.componentHolder.core;

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

public class WorldComponentsState extends PersistentState {

	private static final PersistentState.Type<WorldComponentsState> TYPE =
		new PersistentState.Type<>(WorldComponentsState::createNew, WorldComponentsState::createFromNbt, null);

	private static final String COMPONENT_ID = EuclidsElements.of("components").toString();
	private static final Codec<ComponentMap> COMPONENT_CODEC = ComponentMap.CODEC.optionalFieldOf("components", ComponentMap.EMPTY).codec();

	protected Map<RegistryKey<World>, ComponentMapImpl> worldComponents = new HashMap<>();

	@Nullable
	public static WorldComponentsState get(MinecraftServer server) {
		ServerWorld world = server.getOverworld();
		if (world == null) return null;

		WorldComponentsState state = world.getPersistentStateManager().getOrCreate(TYPE, EuclidsElements.of("components").toString());
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

	public static WorldComponentsState createNew() {
		WorldComponentsState state = new WorldComponentsState();
		state.worldComponents = new HashMap<>();
		return state;
	}

	public static WorldComponentsState createFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		WorldComponentsState state = new WorldComponentsState();
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
