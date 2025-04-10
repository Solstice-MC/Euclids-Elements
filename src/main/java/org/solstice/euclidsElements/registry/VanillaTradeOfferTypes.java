package org.solstice.euclidsElements.registry;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registry;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.api.dataTradeOffer.TradeOfferType;

public class VanillaTradeOfferTypes {

	public static void init() {}

//	public static final Codec<TradeOfferType> BUY_ITEM = register("buy_item", BuyItemOffer.CODEC);

	private static Codec<TradeOfferType> register(String name, Codec<TradeOfferType> codec) {
		return Registry.register(EuclidsRegistries.TRADE_OFFER_TYPE, EuclidsElements.of(name), codec);
	}

}
