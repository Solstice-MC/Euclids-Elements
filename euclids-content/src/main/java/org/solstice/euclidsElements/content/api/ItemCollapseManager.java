package org.solstice.euclidsElements.content.api;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import org.solstice.euclidsElements.content.api.item.CollapsableItem;

import java.util.Iterator;
import java.util.Map;

public class ItemCollapseManager {

	protected final Map<Item, Entry> entries = Maps.newHashMap();
	protected int tick;
	protected final PlayerEntity player;

	public ItemCollapseManager(PlayerEntity player) {
		this.player = player;
	}

	public boolean isCoolingDown(Item item) {
		return this.getCollapseProgress(item, 0) > 0;
	}

	public float getCollapseProgress(Item item, float tickDelta) {
		Entry entry = this.entries.get(item);
		if (entry == null) return 0;

		float f = (float)(entry.endTick - entry.startTick);
		float g = (float)entry.endTick - ((float)this.tick + tickDelta);
		return MathHelper.clamp(g / f, 0.0F, 1.0F);
	}

	public void set(Item item, int duration) {
		this.entries.put(item, new Entry(this.tick, this.tick + duration));
	}

	public void remove(Item item) {
		this.entries.remove(item);
	}

	public void update() {
		++this.tick;
		if (this.entries.isEmpty()) return;

		Iterator<Map.Entry<Item, Entry>> iterator = this.entries.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry<Item, Entry> entry = iterator.next();
			if (entry.getValue().endTick <= this.tick) {
				iterator.remove();
				if (entry.getKey() instanceof CollapsableItem collapsable)
					collapsable.onCollapse(this.player.getWorld(), this.player);
			}
		}

//		this.entries.forEach((item, entry) -> {
//			if (entry.endTick <= this.tick) {
//				this.entries.remove(item);
//				if (item instanceof CollapsableItem collapsable)
//					collapsable.onCollapse(this.player.getWorld(), this.player);
//			}
//		});
	}

	public record Entry(int startTick, int endTick) {}

}
