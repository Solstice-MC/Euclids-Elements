[![Cloudsmith](https://img.shields.io/badge/release%20hosting%20by-cloudsmith-blue?logo=cloudsmith&style=for-the-badge)](https://www.cloudsmith.com)

# About

**Euclid's Elements** is a library mod for creating customizable content mods.

> [!CAUTION]
> The API is not stable yet and may change significantly between versions.

# Usage

Add the Solstice Cloudsmith Maven to your build file and add Euclid's Elements as a dependency.

`build.gradle`
```groovy
repositories {
	maven { url = "https://dl.cloudsmith.io/public/solstice-mc/artifacts/maven/" }
}

dependencies {
	modImplementation "org.solstice:euclids-elements:${euclids_elements_version}"
}
```

# Features (W.I.P.)

## Effect Holder API

Create objects that hold effects the same way Enchantments do by implementing the `EffectHolder` interface.
Make the effects usable by items by making a new item component and implementing `ItemEffectHolder<EffectHolder>`.
Finally, add the item component to the `euclids_elements/tags/data_component_type/effect_holder.json` tag.

## Tooltip Holders

Automatically add item components that implement `TooltipAppender` to item tooltips by adding them to the `euclids_elements/tags/data_component_type/tooltip_holder.json` tag.
