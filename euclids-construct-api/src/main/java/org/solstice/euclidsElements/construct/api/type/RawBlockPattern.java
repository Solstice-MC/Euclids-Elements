package org.solstice.euclidsElements.construct.api.type;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;

import java.util.HashMap;
import java.util.Map;

public record RawBlockPattern(StringPattern pattern, Map<Character, Blockish> keys) {

	public static final Codec<Character> CHAR_CODEC = Codec.STRING.xmap(s -> s.charAt(0), Object::toString);

	public static final Codec<Map<Character, Blockish>> KEY_CODEC = Codec.unboundedMap(CHAR_CODEC, Blockish.CODEC);

	public static final MapCodec<RawBlockPattern> PATTERN_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		StringPattern.CODEC.fieldOf("pattern").forGetter(RawBlockPattern::pattern),
		KEY_CODEC.fieldOf("key").forGetter(RawBlockPattern::keys)
	).apply(instance, RawBlockPattern::new));

	public static final RawBlockPattern EMPTY = new RawBlockPattern(StringPattern.EMPTY, ImmutableMap.of());

	public static RawBlockPattern fromBlockPattern(BlockPattern pattern) {
		if (pattern instanceof SerializableBlockPattern serializablePattern) return serializablePattern.toRawBlockPattern();
		return EMPTY;
	}

	public BlockPattern toBlockPattern() {
		var keys = new HashMap<>(this.keys);
		keys.put(' ', new Blockish.BlockEntry(Blocks.AIR));
		return this.pattern.generateBlockPattern(keys);
	}

}
