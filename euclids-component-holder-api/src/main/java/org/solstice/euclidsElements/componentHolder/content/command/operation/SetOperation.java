package org.solstice.euclidsElements.componentHolder.content.command.operation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.NbtElementArgumentType;
import net.minecraft.command.argument.RegistryKeyArgumentType;
import net.minecraft.component.ComponentType;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.componentHolder.api.AdvancedComponentHolder;
import org.solstice.euclidsElements.componentHolder.content.command.target.TargetType;

import static net.minecraft.server.command.CommandManager.argument;

public class SetOperation implements OperationType {

	public static final SetOperation INSTANCE = new SetOperation();

	@Override
	public String getName() {
		return "set";
	}

	@Override
	public ArgumentBuilder<ServerCommandSource, ?> getArguments(Command<ServerCommandSource> executeFunction) {
		return argument("component_type", RegistryKeyArgumentType.registryKey(RegistryKeys.DATA_COMPONENT_TYPE))
			.then(argument("value", NbtElementArgumentType.nbtElement())
				.executes(executeFunction)
			);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> int execute(CommandContext<ServerCommandSource> context, TargetType target) throws CommandSyntaxException {
		AdvancedComponentHolder holder = target.getHolder(context);
		RegistryEntry<ComponentType<?>> entry = RegistryKeyArgumentType.getRegistryEntry(
			context,
			"component_type",
			RegistryKeys.DATA_COMPONENT_TYPE,
			INVALID_EXCEPTION
		);
		ComponentType<T> component = (ComponentType<T>) entry.value();
		RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, context.getSource().getRegistryManager());
		NbtElement element = NbtElementArgumentType.getNbtElement(context, "value");

		try {
			T value = component.getCodecOrThrow().decode(ops, element).getOrThrow().getFirst();
			holder.set(component, value);
		} catch (Exception exception) {
			EuclidsElements.LOGGER.error("whatever", exception.getCause());
			throw new CommandSyntaxException(FAILED_EXCEPTION, Text.literal(exception.getMessage()));
		}
		Text message = Text.translatable("commands.component." + this.getName() + "." + target.getName() + ".success");
		context.getSource().sendFeedback(() -> message, true);

		return 1;
	}

}
