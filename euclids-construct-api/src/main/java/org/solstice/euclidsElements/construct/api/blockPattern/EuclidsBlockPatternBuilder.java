package org.solstice.euclidsElements.construct.api.blockPattern;

import net.minecraft.block.pattern.BlockPatternBuilder;
import org.solstice.euclidsElements.construct.api.type.Blockish;
import org.solstice.euclidsElements.construct.api.type.StringPattern;

import java.util.Map;

public interface EuclidsBlockPatternBuilder {

	default StringPattern getStringPattern() {
		return StringPattern.EMPTY;
	}

	default Map<Character, Blockish> getKeys() {
		return Map.of();
	}

	BlockPatternBuilder addStringPattern(StringPattern pattern);
	BlockPatternBuilder addKeys(Map<Character, Blockish> keys);

}
