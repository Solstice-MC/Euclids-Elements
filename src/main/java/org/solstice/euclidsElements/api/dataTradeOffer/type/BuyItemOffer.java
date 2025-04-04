package org.solstice.euclidsElements.api.dataTradeOffer.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import org.solstice.euclidsElements.api.dataTradeOffer.TradeOfferType;

public record BuyItemOffer (
	TradedItem stack,
	int price,
	int maxUses,
	int experience,
	float multiplier
) implements TradeOfferType {

	public static final Codec<BuyItemOffer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		TradedItem.CODEC.fieldOf("item").forGetter(BuyItemOffer::stack),
		Codec.INT.optionalFieldOf("price", 1).forGetter(BuyItemOffer::price),
		Codec.INT.optionalFieldOf("maximum_uses", 12).forGetter(BuyItemOffer::maxUses),
		Codec.INT.optionalFieldOf("experience", 0).forGetter(BuyItemOffer::experience),
		Codec.FLOAT.optionalFieldOf("multiplier", 1F).forGetter(BuyItemOffer::multiplier)
	).apply(instance, BuyItemOffer::new));

	@Override
	public TradeOffer create(Entity entity, Random random) {
		return new TradeOffer(this.stack, new ItemStack(Items.EMERALD, this.price), this.maxUses, this.experience, this.multiplier);
	}

}
