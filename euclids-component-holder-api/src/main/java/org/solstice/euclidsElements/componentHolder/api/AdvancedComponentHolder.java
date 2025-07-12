package org.solstice.euclidsElements.componentHolder.api;

import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.component.ComponentType;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public interface AdvancedComponentHolder extends ModifiableComponentHolder {

	default ComponentMapImpl getAdvancedComponents() {
		return null;
	}

	default void setAdvancedComponents(ComponentMapImpl components) {
	}

	@Override
	default ComponentMap getComponents() {
		return this.getAdvancedComponents();
	}

	@Override
	default void setComponents(ComponentMap components) {
		this.setAdvancedComponents(new ComponentMapImpl(components));
	}

	@Override
	@Nullable
	default <T> T get(ComponentType<? extends T> type) {
		return this.getAdvancedComponents().get(type);
	}

	@Override
	default <T> T getOrDefault(ComponentType<? extends T> type, T fallback) {
		return this.getAdvancedComponents().getOrDefault(type, fallback);
	}

	@Override
	default boolean contains(ComponentType<?> type) {
		return this.getAdvancedComponents().contains(type);
	}

	@Nullable
	default <T> T set(ComponentType<? super T> type, @Nullable T value) {
		return this.getAdvancedComponents().set(type, value);
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

	default <T> void remove(ComponentType<? extends T> type) {
		this.getAdvancedComponents().remove(type);
	}

}
