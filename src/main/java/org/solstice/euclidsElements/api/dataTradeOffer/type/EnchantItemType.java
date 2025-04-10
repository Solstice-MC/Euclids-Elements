package org.solstice.euclidsElements.api.dataTradeOffer.type;

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

import java.util.Optional;

public class EnchantItemType implements TradeOffers.Factory {

	private final TradedItem toBeProcessed;
	private final int price;
	private final ItemStack processed;
	private final int maxUses;
	private final int experience;
	private final float multiplier;
	private final Optional<RegistryKey<EnchantmentProvider>> enchantmentProviderKey;

	public EnchantItemType(ItemConvertible item, int count, int price, Item processed, int processedCount, int maxUses, int experience, float multiplier) {
		this(item, count, price, new ItemStack(processed), processedCount, maxUses, experience, multiplier);
	}

	private EnchantItemType(ItemConvertible item, int count, int price, ItemStack processed, int processedCount, int maxUses, int experience, float multiplier) {
		this(new TradedItem(item, count), price, processed.copyWithCount(processedCount), maxUses, experience, multiplier, Optional.empty());
	}

	EnchantItemType(ItemConvertible item, int count, int price, ItemConvertible processed, int processedCount, int maxUses, int experience, float multiplier, RegistryKey<EnchantmentProvider> enchantmentProviderKey) {
		this(new TradedItem(item, count), price, new ItemStack(processed, processedCount), maxUses, experience, multiplier, Optional.of(enchantmentProviderKey));
	}

	public EnchantItemType(TradedItem toBeProcessed, int count, ItemStack processed, int maxUses, int processedCount, float multiplier, Optional<RegistryKey<EnchantmentProvider>> enchantmentProviderKey) {
		this.toBeProcessed = toBeProcessed;
		this.price = count;
		this.processed = processed;
		this.maxUses = maxUses;
		this.experience = processedCount;
		this.multiplier = multiplier;
		this.enchantmentProviderKey = enchantmentProviderKey;
	}

	public TradeOffer create(Entity entity, Random random) {
		ItemStack itemStack = this.processed.copy();
		World world = entity.getWorld();
		this.enchantmentProviderKey.ifPresent((key) -> EnchantmentHelper.applyEnchantmentProvider(itemStack, world.getRegistryManager(), key, world.getLocalDifficulty(entity.getBlockPos()), random));
		return new TradeOffer(new TradedItem(Items.EMERALD, this.price), Optional.of(this.toBeProcessed), itemStack, 0, this.maxUses, this.experience, this.multiplier);
	}

}
