package org.solstice.tabula.autoDatagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.solstice.euclidsElements.autoDatagen.api.generator.*;

public class TabulaAutoDatagen implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();
		pack.addProvider(AutoLanguageGenerator::new);
		pack.addProvider(AutoModelGenerator::new);
		pack.addProvider(AutoLootTableGenerator::new);
	}

}
