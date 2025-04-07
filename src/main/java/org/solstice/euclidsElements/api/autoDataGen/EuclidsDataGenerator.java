package org.solstice.euclidsElements.api.autoDataGen;

import net.minecraft.GameVersion;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.registry.RegistryWrapper;
import net.neoforged.fml.ModContainer;
import org.solstice.euclidsElements.api.autoDataGen.generator.AutoGenerator;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class EuclidsDataGenerator extends DataGenerator {

	public EuclidsDataGenerator(Path outputPath, GameVersion gameVersion, boolean ignoreCache) {
		super(outputPath, gameVersion, ignoreCache);
	}

	@Override
	public <T extends DataProvider> T addProvider(boolean run, DataProvider.Factory<T> factory) {
		return super.addProvider(run, factory);
	}

	public interface Test {

		AutoGenerator create(ModContainer container, DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lookup);

	}

}
