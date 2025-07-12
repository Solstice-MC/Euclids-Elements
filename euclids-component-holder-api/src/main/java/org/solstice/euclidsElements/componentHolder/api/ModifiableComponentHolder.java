package org.solstice.euclidsElements.componentHolder.api;

import net.minecraft.component.ComponentHolder;
import net.minecraft.component.ComponentMap;

public interface ModifiableComponentHolder extends ComponentHolder {

	void setComponents(ComponentMap components);

}
