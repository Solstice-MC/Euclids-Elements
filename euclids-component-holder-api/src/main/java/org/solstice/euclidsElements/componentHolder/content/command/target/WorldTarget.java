package org.solstice.euclidsElements.componentHolder.content.command.target;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.command.argument.RegistryKeyArgumentType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import static net.minecraft.server.command.CommandManager.argument;

public class WorldTarget implements ComponentCommandTarget<World> {

	private static final DynamicCommandExceptionType INVALID_WORLD_EXCEPTION = new DynamicCommandExceptionType((id) -> Text.stringifiedTranslatable("commands.registry_key.world.invalid", id));

	public static final WorldTarget INSTANCE = new WorldTarget();

	@Override
	public String getName() {
		return "world";
	}

	@Override
	public Text getTranslation(World world) {
		return Text.translatable(world.getRegistryKey().getValue().toTranslationKey("world"));
	}

	@Override
	public ArgumentBuilder<ServerCommandSource, ?> getArguments(ArgumentBuilder<ServerCommandSource, ?> child) {
		return argument("world", RegistryKeyArgumentType.registryKey(RegistryKeys.WORLD)).then(child);
	}

	@Override
	public World getHolder(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return RegistryKeyArgumentType.getRegistryEntry(context, "world", RegistryKeys.WORLD, INVALID_WORLD_EXCEPTION).value();
	}

}
