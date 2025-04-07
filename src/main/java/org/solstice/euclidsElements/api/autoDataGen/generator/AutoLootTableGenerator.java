package org.solstice.euclidsElements.api.autoDataGen.generator;

import com.google.common.collect.ImmutableList;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.data.server.loottable.LootTableProvider;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.neoforged.fml.ModContainer;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class AutoLootTableGenerator extends LootTableProvider {

	protected final String modId;

	public AutoLootTableGenerator(ModContainer container, DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> future) {
		super(output, Set.of(), List.of(), future);
		this.modId = container.getModId();
	}

	@Override
	public List<LootTypeGenerator> getTables() {
		return ImmutableList.of(
			new LootTypeGenerator(lookup -> new Block(this.modId, lookup), LootContextTypes.BLOCK)
		);
	}

	public static class Block extends BlockLootTableGenerator implements AutoGenerator {

		protected final String modId;

		public Block(String modId, RegistryWrapper.WrapperLookup lookup) {
			super(Set.of(), FeatureSet.empty(), lookup);
			this.modId = modId;
		}

		@Override
		public String getModId() {
			return this.modId;
		}

		protected void generate() {
			this.registryLookup.getWrapperOrThrow(RegistryKeys.BLOCK)
				.streamEntries()
				.filter(this::ownsEntry)
				.map(RegistryEntry.Reference::value)
				.forEach(this::addDrop);
		}

	}

}
