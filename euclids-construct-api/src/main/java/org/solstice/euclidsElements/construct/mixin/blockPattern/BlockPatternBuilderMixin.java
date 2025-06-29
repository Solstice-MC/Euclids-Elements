package org.solstice.euclidsElements.construct.mixin.blockPattern;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import org.solstice.euclidsElements.construct.api.blockPattern.EuclidsBlockPatternBuilder;
import org.solstice.euclidsElements.construct.api.type.Blockish;
import org.solstice.euclidsElements.construct.api.type.StringPattern;
import org.solstice.euclidsElements.construct.api.type.SerializableBlockPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;
import java.util.function.Predicate;

@Mixin(BlockPatternBuilder.class)
public abstract class BlockPatternBuilderMixin implements EuclidsBlockPatternBuilder {

	@Shadow protected abstract Predicate<CachedBlockPosition>[][][] bakePredicates();

	@Unique private StringPattern pattern;
	@Unique private Map<Character, Blockish> keys;

	@Override
	public StringPattern getStringPattern() {
		return this.pattern;
	}

	@Override
	public Map<Character, Blockish> getKeys() {
		return this.keys;
	}

	@Override
	public BlockPatternBuilder addStringPattern(StringPattern pattern) {
		this.pattern = pattern;
		return (BlockPatternBuilder)(Object)this;
	}

	@Override
	public BlockPatternBuilder addKeys(Map<Character, Blockish> keys) {
		this.keys = keys;
		return (BlockPatternBuilder)(Object)this;
	}

	@WrapMethod(method = "build")
	public BlockPattern build(Operation<BlockPattern> original) {
		return new SerializableBlockPattern(this.bakePredicates(), this.pattern, this.keys);
	}

}
