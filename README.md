[![Cloudsmith](https://img.shields.io/badge/release%20hosting%20by-cloudsmith-blue?logo=cloudsmith&style=for-the-badge)](https://www.cloudsmith.com)

# About

**Euclid's Elements** is a library mod for creating customizable content mods.

# Features

## Effect Holder API

Create objects that hold effects the same way Enchantments do by implementing the `EffectHolder` interface.
Make the effects usable by items by making a new item component and implementing `ItemEffectHolder<EffectHolder>`.
Finally, add the item component to the `euclids_elements/tags/data_component_type/effect_holder.json` tag.

## Tooltip Holders (W.I.P.)

Automatically add item components that implement `TooltipAppender` to item tooltips by adding them to the `euclids_elements/tags/data_component_type/tooltip_holder.json` tag
