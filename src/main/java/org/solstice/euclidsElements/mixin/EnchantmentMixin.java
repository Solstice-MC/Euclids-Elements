package org.solstice.euclidsElements.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.ComponentMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import org.solstice.euclidsElements.api.effectHolder.EffectHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Enchantment.class)
public class EnchantmentMixin implements EffectHolder {

    @Shadow @Final private ComponentMap effects;
    @Shadow @Final private Enchantment.Definition definition;

    @Override
    public ComponentMap getEffects() {
        return this.effects;
    }

    @Override
    public Definition getDefinition() {
        return this.definition;
    }

}
