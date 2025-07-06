package org.solstice.euclidsElements.componentHolder.content.command.operation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.solstice.euclidsElements.componentHolder.api.AdvancedComponentHolder;
import org.solstice.euclidsElements.componentHolder.content.command.target.TargetType;

import java.util.List;

public interface OperationType {

	DynamicCommandExceptionType INVALID_EXCEPTION = new DynamicCommandExceptionType(id -> Text.stringifiedTranslatable("commands.component.invalid", id));
	SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.component.failed"));

	static List<OperationType> values() {
		return List.of(new SetOperation(), new RemoveOperation());
	}

	String getName();

	ArgumentBuilder<ServerCommandSource, ?> getArguments(Command<ServerCommandSource> executeFunction);

	<T> int execute(CommandContext<ServerCommandSource> context, TargetType target) throws CommandSyntaxException;

}
