package org.solstice.euclidsElements.componentHolder.content.command.operation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.RegistryKeyArgumentType;
import net.minecraft.component.ComponentType;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.solstice.euclidsElements.componentHolder.api.AdvancedComponentHolder;
import org.solstice.euclidsElements.componentHolder.content.command.target.ComponentCommandTarget;

import java.util.List;

public interface ComponentCommandOperation {

	DynamicCommandExceptionType INVALID_EXCEPTION = new DynamicCommandExceptionType(id -> Text.stringifiedTranslatable("commands.component.invalid", id));
	SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.component.failed"));

	static List<ComponentCommandOperation> values() {
		return List.of(SetOperation.INSTANCE, GetOperation.INSTANCE, RemoveOperation.INSTANCE);
	}

	String getName();

	ArgumentBuilder<ServerCommandSource, ?> getArguments(Command<ServerCommandSource> executeFunction);

	default <T> void executeOperation(
		CommandContext<ServerCommandSource> context,
		AdvancedComponentHolder holder,
		ComponentType<T> component,
		ComponentCommandTarget<AdvancedComponentHolder> target,
		RegistryOps<NbtElement> registryOps
	) {}

	@SuppressWarnings("unchecked")
	default <T> int execute(CommandContext<ServerCommandSource> context, ComponentCommandTarget<AdvancedComponentHolder> target) throws CommandSyntaxException {
		AdvancedComponentHolder holder = target.getHolder(context);
		if (holder == null) {
			Text message = Text.translatable("commands.component.failed", "Target does not exist");
			throw new CommandSyntaxException(FAILED_EXCEPTION, message);
		}

		RegistryEntry<ComponentType<?>> entry = RegistryKeyArgumentType.getRegistryEntry(
			context,
			"component_type",
			RegistryKeys.DATA_COMPONENT_TYPE,
			INVALID_EXCEPTION
		);

		RegistryOps<NbtElement> registryOps = context.getSource().getRegistryManager().getOps(NbtOps.INSTANCE);
		ComponentType<T> component = (ComponentType<T>) entry.value();

		try {
			this.executeOperation(context, holder, component, target, registryOps);
			return 1;
		} catch (Exception exception) {
			Text message = Text.translatable("commands.component.failed", exception.getMessage());
			throw new CommandSyntaxException(FAILED_EXCEPTION, message);
		}
	}

}
