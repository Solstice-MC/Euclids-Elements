package org.solstice.euclidsElements.construct.api.type;

import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.CachedBlockPosition;

import java.util.Map;
import java.util.function.Predicate;

public class SerializableBlockPattern extends BlockPattern {

	private final StringPattern pattern;
	private final Map<Character, Blockish> keys;

	public SerializableBlockPattern(Predicate<CachedBlockPosition>[][][] blockPattern, StringPattern pattern, Map<Character, Blockish> keys) {
		super(blockPattern);
		this.pattern = pattern;
		this.keys = keys;
	}

	public RawBlockPattern toRawBlockPattern() {
		return new RawBlockPattern(this.pattern, this.keys);
	}

}
