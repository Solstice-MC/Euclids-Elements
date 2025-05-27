package org.solstice.euclidsElements.construct.api.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3i;
import org.solstice.euclidsElements.construct.api.type.Blockish;
import org.solstice.euclidsElements.construct.api.type.Construct;
import org.solstice.euclidsElements.construct.api.type.EntityStack;
import org.solstice.euclidsElements.construct.api.type.StringPattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class ConstructProvider implements DataProvider {

	@Override
	public String getName() {
		return "Constructs";
	}


	protected final FabricDataOutput output;
	protected final CompletableFuture<RegistryWrapper.WrapperLookup> lookupProvider;

	public ConstructProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		this.output = dataOutput;
		this.lookupProvider = registryLookup;
	}


	public abstract void generateConstructs(RegistryWrapper.WrapperLookup registryLookup, ConstructsBuilder builder);


	@Override
	public CompletableFuture<?> run(DataWriter writer) {
		return lookupProvider.thenCompose(lookup -> this.run(writer, lookup));
	}

	public CompletableFuture<?> run(DataWriter writer, RegistryWrapper.WrapperLookup lookup) {
		var builder = new ConstructsBuilder();
		generateConstructs(lookup, builder);
		var constructs = builder.constructData;

		List<CompletableFuture<?>> result = new ArrayList<>();
		constructs.forEach((path, data) -> {
			var id = output
					.getResolver(DataOutput.OutputType.DATA_PACK, "constructs")
					.resolveJson(Identifier.of(output.getModId(), path));
			result.add(DataProvider.writeToPath(writer, data.toJson(), id));
		});

		return CompletableFuture.allOf(result.toArray(CompletableFuture[]::new));
	}


	public static class ConstructsBuilder {

		protected final HashMap<String, Construct> constructData;

		private ConstructsBuilder() {
			this.constructData = new HashMap<>();
		}

		public ConstructsBuilder add(String path, Construct data) {
			constructData.put(path, data);
			return this;
		}

		public ConstructsBuilder add(
				String path,
				StringPattern pattern,
				Map<Character, Blockish> keys,
				Vec3i offset,
				EntityStack entity
		) {
			var data = Construct.create(pattern, keys, offset, entity);
			add(path, data);
			return this;
		}

		public ConstructsBuilder add(
				String path,
				StringPattern pattern,
				Map<Character, Blockish> keys,
				EntityStack entity
		) {
			var data = Construct.create(pattern, keys, Vec3i.ZERO, entity);
			add(path, data);
			return this;
		}

		public ConstructsBuilder add(
				String path,
				BlockPattern pattern,
				Vec3i offset,
				EntityStack entity
		) {
			var data = new Construct(pattern, offset, entity);
			add(path, data);
			return this;
		}

		public ConstructsBuilder add(
				String path,
				BlockPattern pattern,
				EntityStack entity
		) {
			var data = new Construct(pattern, Vec3i.ZERO, entity);
			add(path, data);
			return this;
		}

	}

}
