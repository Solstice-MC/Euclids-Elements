package org.solstice.euclidsElements.componentHolder.content.command.target;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.solstice.euclidsElements.componentHolder.api.MutableComponentHolder;

import java.util.List;

public interface ComponentCommandTarget<T extends MutableComponentHolder> {

	static List<ComponentCommandTarget<?>> values() {
		return List.of(ItemTarget.INSTANCE, EntityTarget.INSTANCE, WorldTarget.INSTANCE);
	}

	String getName();

	ArgumentBuilder<ServerCommandSource, ?> getArguments(ArgumentBuilder<ServerCommandSource, ?> child);

	T getHolder(CommandContext<ServerCommandSource> context) throws CommandSyntaxException;

	Text getTranslation(T holder);

}
