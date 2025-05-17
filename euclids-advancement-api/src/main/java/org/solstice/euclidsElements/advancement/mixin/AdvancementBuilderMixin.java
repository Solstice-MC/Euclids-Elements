package org.solstice.euclidsElements.advancement.mixin;

import com.google.common.collect.ImmutableMap;
import net.minecraft.advancement.*;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.advancement.api.EuclidsAdvancementBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;
import java.util.function.UnaryOperator;

@Mixin(Advancement.Builder.class)
public class AdvancementBuilderMixin implements EuclidsAdvancementBuilder {

	@Shadow private Optional<Identifier> parentObj = Optional.empty();
	@Shadow private Optional<AdvancementDisplay> display = Optional.empty();
	@Shadow private AdvancementRewards rewards;
	@Shadow @Final private ImmutableMap.Builder<String, AdvancementCriterion<?>> criteria;
	@Shadow private Optional<AdvancementRequirements> requirements;
	@Shadow private AdvancementRequirements.CriterionMerger merger;
	@Shadow private boolean sendsTelemetryEvent;

	@Override
	public Advancement.Builder requirements(List<String> requirement) {
		this.modifyRequirements(requirements -> {
			requirements.add(requirement);
			return requirements;
		});
		return ((Advancement.Builder)(Object)this);
	}

	@Override
	public Advancement.Builder requirements(String... requirement) {
		this.requirements(Arrays.stream(requirement).toList());
		return ((Advancement.Builder)(Object)this);
	}

	@Override
	public Advancement.Builder modifyRequirements(UnaryOperator<List<List<String>>> function) {
		List<List<String>> originalRequirements = this.requirements
			.map(requirements -> new ArrayList<>(requirements.requirements()))
			.orElseGet(ArrayList::new);
		originalRequirements = function.apply(originalRequirements);
		AdvancementRequirements requirements = new AdvancementRequirements(originalRequirements);
		this.requirements = Optional.of(requirements);
		return ((Advancement.Builder)(Object)this);
	}

	@Override
	public Advancement build() {
		Map<String, AdvancementCriterion<?>> map = this.criteria.buildOrThrow();
		AdvancementRequirements advancementRequirements = this.requirements.orElseGet(() -> this.merger.create(map.keySet()));
		return new Advancement(this.parentObj, this.display, this.rewards, map, advancementRequirements, this.sendsTelemetryEvent);
	}

}
