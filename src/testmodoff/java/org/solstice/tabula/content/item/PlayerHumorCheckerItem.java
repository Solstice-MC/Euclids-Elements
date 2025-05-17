package org.solstice.tabula.content.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.solstice.tabula.content.HumorValue;
import org.solstice.tabula.registry.TabulaComponentTypes;

public class PlayerHumorCheckerItem extends Item {

	public PlayerHumorCheckerItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);

		HumorValue humorous = player.getOrDefault(TabulaComponentTypes.HUMOROUS, HumorValue.UNSATISFACTORY);
		if (player.isSneaking()) {
			humorous = humorous.next();
			player.set(TabulaComponentTypes.HUMOROUS, humorous);
		}

		Text test = Text.of(humorous.asString());
		player.sendMessage(test, true);
		return TypedActionResult.pass(stack);
	}

}
