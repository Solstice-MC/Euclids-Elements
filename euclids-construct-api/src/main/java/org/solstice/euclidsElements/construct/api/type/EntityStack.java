package org.solstice.euclidsElements.construct.api.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public record EntityStack(EntityType<?> type, NbtCompound data) {

	public static final Codec<EntityStack> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Registries.ENTITY_TYPE.getCodec().fieldOf("id").forGetter(EntityStack::type),
		NbtCompound.CODEC.optionalFieldOf("data", new NbtCompound()).forGetter(EntityStack::data)
	).apply(instance, EntityStack::new));

	public @Nullable Entity create(World world) {
		Entity entity = type.create(world);
		if (entity != null) entity.readNbt(data);
		return entity;
	}

}
