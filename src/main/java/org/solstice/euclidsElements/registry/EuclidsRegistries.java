package org.solstice.euclidsElements.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.Registry;
import net.minecraft.registry.SimpleRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.solstice.euclidsElements.api.dataTradeOffer.TradeOfferType;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class EuclidsRegistries {

	public static final Registry<Codec<TradeOfferType>> TRADE_OFFER_TYPE = new SimpleRegistry<>(EuclidsRegistryKeys.TRADE_OFFER_TYPE, Lifecycle.stable());

	@SubscribeEvent
	public static void registerBuiltinRegistries(NewRegistryEvent event) {
		event.register(TRADE_OFFER_TYPE);
	}

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
//        event.dataPackRegistry(EuclidsRegistryKeys.UPGRADE, Upgrade.CODEC, Upgrade.CODEC);
    }

}
