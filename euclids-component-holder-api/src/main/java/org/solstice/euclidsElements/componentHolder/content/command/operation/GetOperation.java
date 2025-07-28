package org.solstice.euclidsElements.componentHolder.content.command.operation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.RegistryKeyArgumentType;
import net.minecraft.component.ComponentType;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.solstice.euclidsElements.componentHolder.api.AdvancedComponentHolder;
import org.solstice.euclidsElements.componentHolder.content.command.target.ComponentCommandTarget;

import static net.minecraft.server.command.CommandManager.argument;

public class GetOperation implements ComponentCommandOperation {

	public static final GetOperation INSTANCE = new GetOperation();

	@Override
	public String getName() {
		return "get";
	}

	@Override
	public ArgumentBuilder<ServerCommandSource, ?> getArguments(Command<ServerCommandSource> executeFunction) {
		return argument("component_type", RegistryKeyArgumentType.registryKey(RegistryKeys.DATA_COMPONENT_TYPE))
			.executes(executeFunction);
	}

	@Override
	public <T> void executeOperation(
		CommandContext<ServerCommandSource> context,
		AdvancedComponentHolder holder,
		ComponentType<T> component,
		ComponentCommandTarget<AdvancedComponentHolder> target,
		RegistryOps<NbtElement> registryOps
	) {
		NbtElement element = component.getCodecOrThrow().encodeStart(registryOps, holder.get(component)).getOrThrow();
		Text message = Text.translatable("commands.component.get.success",
			target.getTranslation(holder),
			Text.literal(component.toString()).formatted(Formatting.AQUA),
			NbtHelper.toPrettyPrintedText(element)
		);
		context.getSource().sendFeedback(() -> message, true);
	}

}
