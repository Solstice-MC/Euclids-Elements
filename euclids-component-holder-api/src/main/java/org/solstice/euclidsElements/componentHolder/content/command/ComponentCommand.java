package org.solstice.euclidsElements.componentHolder.content.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import org.solstice.euclidsElements.componentHolder.content.command.operation.OperationType;
import org.solstice.euclidsElements.componentHolder.content.command.target.TargetType;

import static net.minecraft.server.command.CommandManager.literal;

public class ComponentCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access) {
		LiteralArgumentBuilder<ServerCommandSource> builder = literal("component")
			.requires(source -> source.hasPermissionLevel(2));

		for (OperationType operation : OperationType.values()) {
			ArgumentBuilder<ServerCommandSource, ?> operationBuilder = literal(operation.getName());

			for (TargetType target : TargetType.values()) {
				ArgumentBuilder<ServerCommandSource, ?> targetBuilder = literal(target.getName());

				targetBuilder.then(
					target.getArguments(
						operation.getArguments(context -> operation.execute(context, target))
					)
				);

				operationBuilder.then(targetBuilder);
			}

			builder.then(operationBuilder);
		}

		dispatcher.register(builder);
	}

}
