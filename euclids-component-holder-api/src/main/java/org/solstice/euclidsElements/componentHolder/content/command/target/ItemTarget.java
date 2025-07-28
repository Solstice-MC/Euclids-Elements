package org.solstice.euclidsElements.componentHolder.content.command.target;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ItemSlotArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;

public class ItemTarget implements ComponentCommandTarget<ItemStack> {

	public static final ItemTarget INSTANCE = new ItemTarget();

	@Override
	public String getName() {
		return "item";
	}

	@Override
	public Text getTranslation(ItemStack stack) {
		return stack.getName();
	}

	@Override
	public ArgumentBuilder<ServerCommandSource, ?> getArguments(ArgumentBuilder<ServerCommandSource, ?> child) {
		return argument("target", EntityArgumentType.entity())
			.then(argument("slot", ItemSlotArgumentType.itemSlot())
				.then(child)
			);
	}

	@Override
	public ItemStack getHolder(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Entity entity = EntityArgumentType.getEntity(context, "target");
		if (!(entity instanceof LivingEntity target)) throw new CommandSyntaxException(EntityArgumentType.NOT_ALLOWED_EXCEPTION, Text.translatable("commands.component.set.item.invalid_target"));

		int slot = ItemSlotArgumentType.getItemSlot(context, "slot");
		return target.getStackReference(slot).get();
	}

}
