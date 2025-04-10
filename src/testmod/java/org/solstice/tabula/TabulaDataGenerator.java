package org.solstice.tabula;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.solstice.euclidsElements.api.autoDataGen.generator.AutoLanguageGenerator;
import org.solstice.euclidsElements.api.autoDataGen.generator.AutoLootTableGenerator;

public class TabulaDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();
		pack.addProvider(AutoLanguageGenerator::new);
		pack.addProvider(AutoLootTableGenerator::new);
	}

}
