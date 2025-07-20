package org.solstice.euclidsElements.content.api.item.bundle;

import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

public interface EuclidsBundleItem {

	/**
	 * Gets the maximum capacity of the bundle
	 *
	 * @return The maximum capacity of the bundle
	 */
	default int getCapacity() {
		return 64;
	}

	/**
	 * Determines whether an {@link ItemStack} can be inserted into the bundle
	 *
	 * @param stack The item stack to check
	 * @return {@code true} if the stack can be inserted into the bundle, {@code false} otherwise
	 */
	default boolean acceptsStack(ItemStack stack) {
		return !stack.isEmpty();
	}

	/**
	 * Calculates how much space an {@link ItemStack} occupies within the bundle
	 *
	 * @param stack The stack to calculate occupancy for
	 * @return A fraction representing how much space the stack occupies in the bundle
	 */
	default Fraction getStackOccupancy(ItemStack stack) {
		Fraction result = BundleContentsComponent.getOccupancy(stack);
		Fraction defaultResult = Fraction.getFraction(1, stack.getMaxCount());
		if (!result.equals(defaultResult)) return result;

		int percent = (int) Math.ceil((double) stack.getMaxCount() / this.getCapacity());
		return Fraction.getFraction(percent, this.getCapacity());
	}

}
