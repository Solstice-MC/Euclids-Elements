package org.solstice.euclidsElements.mixin;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
import net.minecraft.world.level.Level;
import org.solstice.euclidsElements.api.effectHolder.EffectHolder;
import org.solstice.euclidsElements.api.effectHolder.entity.EntityEffectHolderData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;
import java.util.Set;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements EntityEffectHolderData {

    @Unique private final Reference2ObjectMap<EffectHolder, Set<EnchantmentLocationBasedEffect>> locationBasedEffects = new Reference2ObjectArrayMap<>();

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    public Map<EffectHolder, Set<EnchantmentLocationBasedEffect>> getLocationBasedEffects() {
        return this.locationBasedEffects;
    }

}
