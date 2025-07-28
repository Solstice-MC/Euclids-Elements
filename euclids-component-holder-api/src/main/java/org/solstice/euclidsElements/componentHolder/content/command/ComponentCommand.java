package org.solstice.euclidsElements.componentHolder.content.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import org.solstice.euclidsElements.componentHolder.content.command.operation.ComponentCommandOperation;
import org.solstice.euclidsElements.componentHolder.content.command.target.ComponentCommandTarget;

import static net.minecraft.server.command.CommandManager.literal;

public class ComponentCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access) {
		LiteralArgumentBuilder<ServerCommandSource> builder = literal("component")
			.requires(source -> source.hasPermissionLevel(2));

		for (ComponentCommandOperation operation : ComponentCommandOperation.values()) {
			ArgumentBuilder<ServerCommandSource, ?> operationBuilder = literal(operation.getName());

			for (ComponentCommandTarget target : ComponentCommandTarget.values()) {
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
