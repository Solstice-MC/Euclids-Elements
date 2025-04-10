package org.solstice.euclidsElements.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.Registry;
import net.minecraft.registry.SimpleRegistry;
import org.solstice.euclidsElements.api.dataTradeOffer.TradeOfferType;

public class EuclidsRegistries {

	public static void init() {
//		DynamicRegistries.register(TRADE_OFFER_TYPE, TradeOfferType.CODEC);
	}

	public static final Registry<Codec<TradeOfferType>> TRADE_OFFER_TYPE = new SimpleRegistry<>(EuclidsRegistryKeys.TRADE_OFFER_TYPE, Lifecycle.stable());

}
