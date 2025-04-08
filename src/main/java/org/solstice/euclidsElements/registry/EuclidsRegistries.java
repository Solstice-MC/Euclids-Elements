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
import org.solstice.euclidsElements.api.dataTradeOffer.TradeOfferType;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class EuclidsRegistries {

	public static final ResourceKey<Registry<Codec<TradeOfferType>>> TRADE_OFFER_TYPE = of("trade_offer_type");

	public static final Registry<Codec<TradeOfferType>> TRADE_OFFER_TYPE_REGISTRY = new MappedRegistry<>(TRADE_OFFER_TYPE, Lifecycle.stable());

	@SubscribeEvent
	public static void registerBuiltinRegistries(NewRegistryEvent event) {
		event.register(TRADE_OFFER_TYPE_REGISTRY);
	}

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
//        event.dataPackRegistry(EuclidsRegistryKeys.UPGRADE, Upgrade.CODEC, Upgrade.CODEC);
    }

	public static <T> ResourceKey<Registry<T>> of(String name) {
		return ResourceKey.createRegistryKey(EuclidsElements.of(name));
	}

}
