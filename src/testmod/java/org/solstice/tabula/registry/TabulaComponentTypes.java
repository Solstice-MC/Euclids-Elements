package org.solstice.tabula.registry;

import net.minecraft.component.ComponentType;
import org.solstice.euclidsElements.content.test.ComponentRegistryContainer;
import org.solstice.tabula.Tabula;
import org.solstice.tabula.content.HumorValue;

public class TabulaComponentTypes {

	protected static final ComponentRegistryContainer CONTAINER = Tabula.REGISTRY.component();

	public static void init() {}

	public static final ComponentType<HumorValue> HUMOROUS = CONTAINER.register("humorous", HumorValue.CODEC);

}
