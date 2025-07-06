package org.solstice.euclidsElements.componentHolder.content.command.target;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import org.solstice.euclidsElements.componentHolder.api.AdvancedComponentHolder;

import java.util.List;

public interface TargetType {

	static List<TargetType> values() {
		return List.of(ItemTarget.INSTANCE, EntityTarget.INSTANCE);
	}

	String getName();

	ArgumentBuilder<ServerCommandSource, ?> getArguments(ArgumentBuilder<ServerCommandSource, ?> child);

	AdvancedComponentHolder getHolder(CommandContext<ServerCommandSource> context) throws CommandSyntaxException;

}
