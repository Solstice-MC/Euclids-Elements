package org.solstice.euclidsElements.componentHolder.api;

import net.minecraft.component.ComponentMapImpl;
import net.minecraft.component.ComponentType;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public interface AdvancedComponentHolder {

	default ComponentMapImpl getComponents() {
		return null;
	}

	@Nullable
	default <T> T get(ComponentType<? extends T> type) {
		return this.getComponents().get(type);
	}

	default <T> T getOrDefault(ComponentType<? extends T> type, T fallback) {
		return this.getComponents().getOrDefault(type, fallback);
	}

	default boolean contains(ComponentType<?> type) {
		return this.getComponents().contains(type);
	}

	@Nullable
	default <T> T set(ComponentType<? super T> type, @Nullable T value) {
		return this.getComponents().set(type, value);
	}

	@Nullable
	default <T, U> T apply(ComponentType<T> type, T defaultValue, U change, BiFunction<T, U, T> applier) {
		return this.set(type, applier.apply(this.getOrDefault(type, defaultValue), change));
	}

	@Nullable
	default <T> T apply(ComponentType<T> type, T defaultValue, UnaryOperator<T> applier) {
		T object = this.getOrDefault(type, defaultValue);
		return this.set(type, applier.apply(object));
	}

	@Nullable
	default <T> T remove(ComponentType<? extends T> type) {
		return this.getComponents().remove(type);
	}

}
