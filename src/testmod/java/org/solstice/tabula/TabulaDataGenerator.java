package org.solstice.tabula;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.solstice.euclidsElements.api.autoDataGen.generator.*;

public class TabulaDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();
		pack.addProvider(AutoLanguageGenerator::new);
//		pack.addProvider(AutoItemModelGenerator::new);
//		pack.addProvider(AutoBlockModelGenerator::new);
		pack.addProvider(AutoModelGenerator::new);
		pack.addProvider(AutoLootTableGenerator::new);
	}

}
