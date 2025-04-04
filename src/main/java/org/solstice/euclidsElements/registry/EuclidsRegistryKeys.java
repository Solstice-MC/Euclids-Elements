package org.solstice.euclidsElements.registry;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.api.dataTradeOffer.TradeOfferType;

public class EuclidsRegistryKeys {

    public static final RegistryKey<Registry<Codec<TradeOfferType>>> TRADE_OFFER_TYPE = of("trade_offer_type");

	public static <T> RegistryKey<Registry<T>> of(String name) {
        return RegistryKey.ofRegistry(EuclidsElements.of(name));
    }

}
