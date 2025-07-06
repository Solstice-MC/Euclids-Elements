package org.solstice.euclidsElements.componentHolder.content.command.target;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import org.solstice.euclidsElements.componentHolder.api.AdvancedComponentHolder;

import static net.minecraft.server.command.CommandManager.argument;

public class EntityTarget implements TargetType {

	public static final EntityTarget INSTANCE = new EntityTarget();

	@Override
	public String getName() {
		return "entity";
	}

	@Override
	public ArgumentBuilder<ServerCommandSource, ?> getArguments(ArgumentBuilder<ServerCommandSource, ?> child) {
		return argument("target", EntityArgumentType.entity()).then(child);
	}

	@Override
	public AdvancedComponentHolder getHolder(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return EntityArgumentType.getEntity(context, "target");
	}

}
