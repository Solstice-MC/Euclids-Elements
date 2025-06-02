package org.solstice.euclidsElements.content.api.item;

public interface FuelItemStack {

	default boolean isFuel() {
		return false;
	}

	default int getFuelTime() {
		return 0;
	}

}
