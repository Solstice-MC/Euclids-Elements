package org.solstice.euclidsElements.componentHolder.api;

import net.minecraft.component.ComponentHolder;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.component.ComponentType;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public interface MutableComponentHolder extends ComponentHolder {

	void setComponents(ComponentMapImpl components);

	ComponentMapImpl getMutableComponents();

	default void setImmutableComponents(ComponentMap components) {
		this.setComponents(new ComponentMapImpl(components));
	}

	@Override
	default ComponentMap getComponents() {
		return this.getMutableComponents();
	}

	@Nullable
	default <T> T set(ComponentType<? super T> type, @Nullable T value) {
		return this.getMutableComponents().set(type, value);
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
		this.getMutableComponents().remove(type);
	}

}
