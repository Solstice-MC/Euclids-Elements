package org.solstice.euclidsElements.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.solstice.euclidsElements.api.effectHolder.item.component.EffectHolderComponent;
import org.spongepowered.asm.mixin.*;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(ItemEnchantmentsComponent.class)
public class ItemEnchantmentsComponentMixin implements EffectHolderComponent<Enchantment> {

    @Shadow @Final Object2IntOpenHashMap<RegistryEntry<Enchantment>> enchantments;

    @Override
    public Object2IntOpenHashMap<RegistryEntry<Enchantment>> getEffects() {
        return this.enchantments;
    }

}
