package org.solstice.tabula.content.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.solstice.tabula.content.HumorValue;
import org.solstice.tabula.registry.TabulaTags;

public class HumorCheckerItem extends Item {

	public HumorCheckerItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		PlayerEntity player = context.getPlayer();
		if (player == null) return ActionResult.PASS;

		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		Block block = world.getBlockState(pos).getBlock();

		HumorValue humorous = Registries.BLOCK.getEntry(block).getMapValue(TabulaTags.HUMOROUS);
		if (humorous == null) {
			player.sendMessage(Text.translatable("block.humorous.unknown").formatted(Formatting.GREEN), true);
			return ActionResult.PASS;
		}

		player.sendMessage(Text.translatable("block.humorous." + humorous).formatted(Formatting.GREEN), true);
		return ActionResult.SUCCESS;
	}

}
