package org.solstice.tabula;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.solstice.euclidsElements.api.autoDataGen.generator.AutoItemModelGenerator;
import org.solstice.euclidsElements.api.autoDataGen.generator.AutoLanguageGenerator;
import org.solstice.euclidsElements.api.autoDataGen.provider.EuclidsLanguageProvider;

import java.io.DataOutput;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class TabulaDataGenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		boolean server = event.includeServer();
		DataGenerator generator = event.getGenerator();

		ModContainer container = event.getModContainer();
		PackOutput output = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		generator.addProvider(server, new AutoLanguageGenerator(container, output, lookup));
//		generator.addProvider(server, new Test(container, output, lookup));
		generator.addProvider(server, new AutoItemModelGenerator(container, output, helper));
	}

	public static class Test extends EuclidsLanguageProvider {

		public Test(ModContainer container, PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
			super(container, output, lookup);
		}

		@Override
		protected void addTranslations(HolderLookup.Provider lookup) {
			this.add("test", "test");
		}

	}

}
