package org.solstice.euclidsElements.api.autoDataGen.generator;


import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.fml.ModContainer;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class AutoBlockLootTableGenerator extends LootTableProvider {

	public AutoBlockLootTableGenerator(ModContainer container, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(
			output,
			Set.of(),
			List.of(
				new SubProviderEntry(provider -> new Block(container, provider), LootContextParamSets.BLOCK)
			),
			registries
		);
	}



	public static class Block extends BlockLootSubProvider implements AutoGenerator {

		@Override
		public ModContainer getContainer() {
			return this.container;
		}

		protected final ModContainer container;

		protected Block(ModContainer container, HolderLookup.Provider provider) {
			super(Collections.emptySet(), FeatureFlagSet.of(), provider);
			this.container = container;
		}

		@Override
		public void generate() {
			BuiltInRegistries.BLOCK.holders()
				.filter(this::ownsEntry)
				.map(Holder.Reference::value)
				.forEach(this::dropSelf);
		}

	}



//	public AutoBlockLootTableGenerator(ModContainer container) {
//		this.container = container;
//	}

//	@Override
//	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer) {
//		BuiltInRegistries.BLOCK.holders()
//			.filter(this::ownsEntry)
//			.forEach(this::generateBlockLootTable);
//	}
//
//	public void generateBlockLootTable(Holder<Block> entry) {
//		Block block = entry.value();
//		ResourceLocation id = entry.getKey().location();
//		this.
//		BlockModelSupplier.generate(this, block, id);
//	}

}
