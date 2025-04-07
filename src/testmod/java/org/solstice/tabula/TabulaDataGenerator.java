package org.solstice.tabula;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class TabulaDataGenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
//		ModContainer container = event.getModContainer();
//		DataGenerator generator = event.getGenerator();
//		DataOutput output = generator.getPackOutput();
//		CompletableFuture<RegistryWrapper.WrapperLookup> lookup = event.getLookupProvider();

//		generator.addProvider(event.includeServer(), new AutoLanguageGenerator(container, output, lookup));
//		generator.addProvider(event.includeServer(), new AutoItemModelGenerator(container, output, lookup));
	}

}
