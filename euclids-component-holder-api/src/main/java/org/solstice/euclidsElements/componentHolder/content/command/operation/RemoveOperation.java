package org.solstice.euclidsElements.componentHolder.content.command.operation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.NbtElementArgumentType;
import net.minecraft.command.argument.RegistryKeyArgumentType;
import net.minecraft.component.ComponentType;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.componentHolder.api.MutableComponentHolder;
import org.solstice.euclidsElements.componentHolder.content.command.target.ComponentCommandTarget;

import static net.minecraft.server.command.CommandManager.argument;

public class RemoveOperation implements ComponentCommandOperation {

	public static final RemoveOperation INSTANCE = new RemoveOperation();

	@Override
	public String getName() {
		return "remove";
	}

	@Override
	public ArgumentBuilder<ServerCommandSource, ?> getArguments(Command<ServerCommandSource> executeFunction) {
		return argument("component_type", RegistryKeyArgumentType.registryKey(RegistryKeys.DATA_COMPONENT_TYPE))
			.executes(executeFunction);
	}

	@Override
	public <T> void executeOperation(
		CommandContext<ServerCommandSource> context,
		MutableComponentHolder holder,
		ComponentType<T> component,
		ComponentCommandTarget<MutableComponentHolder> target,
		RegistryOps<NbtElement> registryOps
	) {
		holder.remove(component);

		Text message = Text.translatable("commands.component.remove.success",
			Text.literal(component.toString()).formatted(Formatting.AQUA),
			target.getTranslation(holder)
		);
		context.getSource().sendFeedback(() -> message, true);
	}

}
