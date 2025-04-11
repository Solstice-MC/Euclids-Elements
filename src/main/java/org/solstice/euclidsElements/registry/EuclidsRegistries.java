package org.solstice.euclidsElements.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.solstice.euclidsElements.EuclidsElements;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class EuclidsRegistries {

	@SubscribeEvent
	public static void registerBuiltinRegistries(NewRegistryEvent event) {}

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {}

	public static <T> ResourceKey<Registry<T>> of(String name) {
		return ResourceKey.createRegistryKey(EuclidsElements.of(name));
	}

}
