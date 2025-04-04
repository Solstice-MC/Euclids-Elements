package org.solstice.euclidsElements.registry;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.api.dataTradeOffer.TradeOfferType;

public class VanillaTradeOfferTypes {

	public static final DeferredRegister<Codec<TradeOfferType>> REGISTRY = DeferredRegister
		.create(EuclidsRegistries.TRADE_OFFER_TYPE, EuclidsElements.MOD_ID);

//	public static final Codec<TradeOfferType> BUY_ITEM = register("buy_item", BuyItemOffer.CODEC);

	private static Codec<TradeOfferType> register(String name, Codec<TradeOfferType> codec) {
		REGISTRY.register(name, () -> codec);
		return codec;
	}

}
