package org.solstice.euclidsElements.mixin;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import org.solstice.euclidsElements.api.effectHolder.EffectHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(Enchantment.EnchantmentDefinition.class)
public class EnchantmentDefinitionMixin implements EffectHolder.Definition {

    @Shadow @Final private int maxLevel;
    @Shadow @Final private List<EquipmentSlotGroup> slots;

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }

    @Override
    public List<EquipmentSlotGroup> getSlots() {
        return this.slots;
    }

}
