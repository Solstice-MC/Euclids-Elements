package org.solstice.tabula.content.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.content.api.entity.PlayerItemCollapseManager;
import org.solstice.euclidsElements.content.api.item.CollapsableItem;

public class WeightsItem extends Item implements CollapsableItem {

	public static void register() {
		Registry.register(Registries.ITEM, EuclidsElements.of("weights"), new WeightsItem(new Settings()));
	}

	public WeightsItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		((PlayerItemCollapseManager)context.getPlayer()).getItemCollapseManager().set(this, 50);
		return ActionResult.SUCCESS;
	}

	@Override
	public void onCollapse(World world, PlayerEntity player) {
		player.getItemCooldownManager().set(this, 50);
		if (world.isClient()) return;
		player.sendMessage(Text.literal("Item Collapsed!"), true);
	}

}
