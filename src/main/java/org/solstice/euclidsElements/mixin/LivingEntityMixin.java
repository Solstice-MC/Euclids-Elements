package org.solstice.euclidsElements.mixin;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.minecraft.enchantment.effect.EnchantmentLocationBasedEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.solstice.euclidsElements.api.effectHolder.EffectHolder;
import org.solstice.euclidsElements.api.effectHolder.entity.EntityEffectHolderData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;
import java.util.Set;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements EntityEffectHolderData {

    @Unique private final Reference2ObjectMap<EffectHolder, Set<EnchantmentLocationBasedEffect>> locationBasedEffects = new Reference2ObjectArrayMap<>();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public Map<EffectHolder, Set<EnchantmentLocationBasedEffect>> getLocationBasedEffects() {
        return this.locationBasedEffects;
    }

}
