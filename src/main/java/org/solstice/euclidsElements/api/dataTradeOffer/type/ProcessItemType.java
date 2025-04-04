package org.solstice.euclidsElements.api.dataTradeOffer.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.provider.EnchantmentProvider;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TradedItem;
import net.minecraft.world.World;
import org.solstice.euclidsElements.api.dataTradeOffer.TradeOfferType;

import javax.annotation.Nullable;
import java.util.Optional;

public record ProcessItemType (
	TradedItem toBeProcessed,
	int price,
	ItemStack processed,
	int maxUses,
	int experience,
	float multiplier
) implements TradeOffers.Factory {

	public static final Codec<ProcessItemType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		TradedItem.CODEC.fieldOf("item").forGetter(null),
		Codec.INT.fieldOf("price").forGetter(null),
		ItemStack.CODEC.fieldOf("result").forGetter(null),
		Codec.INT.optionalFieldOf("maximum_uses", 12).forGetter(null),
		Codec.INT.optionalFieldOf("experience", 0).forGetter(null),
		Codec.FLOAT.optionalFieldOf("multiplier", 1F).forGetter(null)
	).apply(instance, ProcessItemType::new));

	@Override
	public TradeOffer create(Entity entity, Random random) {
		ItemStack stack = this.processed.copy();
		return new TradeOffer(new TradedItem(Items.EMERALD, this.price), Optional.of(this.toBeProcessed), stack, 0, this.maxUses, this.experience, this.multiplier);
	}

}
