package org.solstice.euclidsElements.construct.api.type;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import org.solstice.euclidsElements.construct.api.entity.ConstructableEntity;

public record Construct(BlockPattern pattern, Vec3i offset, EntityStack entity) {

	public static final MapCodec<BlockPattern> PATTERN_CODEC = RawBlockPattern.PATTERN_CODEC.xmap(
		RawBlockPattern::toBlockPattern,
		RawBlockPattern::fromBlockPattern
	);

	public static final Codec<Construct> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			PATTERN_CODEC.forGetter(Construct::pattern),
			Vec3i.CODEC.fieldOf("offset").forGetter(Construct::offset),
			EntityStack.CODEC.fieldOf("entity").forGetter(Construct::entity)
	).apply(instance, Construct::new));

	public JsonObject toJson() {
		return CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow().getAsJsonObject();
	}

	public boolean canConstruct(WorldView world, BlockPos pos) {
		return this.pattern.searchAround(world, pos) != null;
	}

	public void trySpawn(World world, BlockPos pos) {
		BlockPattern.Result result = this.matchPattern(world, pos);
		if (result == null) return;

		Entity entity = this.createEntity(world);
		if (entity == null) return;

		CarvedPumpkinBlock.spawnEntity(world, result, entity, this.offsetPosition(result));
		if (entity instanceof ConstructableEntity constructable) constructable.onConstructed(result, world, pos);
	}

	public BlockPos offsetPosition(BlockPattern.Result result) {
		return result.translate(this.offset.getX(), this.offset.getY(), this.offset.getZ()).getBlockPos();
	}

	@Nullable
	public BlockPattern.Result matchPattern(WorldView world, BlockPos pos) {
		return this.pattern.searchAround(world, pos);
	}

	@Nullable
	public Entity createEntity(World world) {
		return this.entity.create(world);
	}

}
