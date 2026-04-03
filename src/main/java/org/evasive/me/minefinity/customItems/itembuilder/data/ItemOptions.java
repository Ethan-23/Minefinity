package org.evasive.me.minefinity.customItems.itembuilder.data;

import org.bukkit.Material;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.*;
import org.evasive.me.minefinity.mining.abilities.PickaxeAbilities;
import org.evasive.me.minefinity.core.rarity.Rarity;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public enum ItemOptions {
    MINEFINITY_ID(
            Material.NAME_TAG,
            String.class,
            BaseCustomItem::getID,
            (builder, value) -> builder.setId(((String) value).toUpperCase())
    ),
    MATERIAL(
            Material.CRAFTING_TABLE,
            String.class,
            BaseCustomItem::getMaterial,
            (builder, value) -> builder.setMaterial(Material.valueOf(((String) value).toUpperCase()))
    ),
    DISPLAY_NAME(
            Material.WRITABLE_BOOK,
            String.class,
            BaseCustomItem::getDisplayName,
            (builder, value) -> builder.setDisplayName((String) value)
    ),
    CUSTOM_ITEM_TYPE(
            Material.KNOWLEDGE_BOOK,
            CustomItemType.class,
            BaseCustomItem::getCustomItemType,
            (builder, value) -> builder.setItemType((CustomItemType) value)
    ),
    RARITY(
            Material.GRAY_DYE,
            Rarity.class,
            BaseCustomItem::getRarity,
            (builder, value) -> builder.setRarity((Rarity) value)
    ),
    MINING_SPEED(
            Material.GOLDEN_PICKAXE,
            Float.class,
            builder -> {
                if (builder instanceof BasePickaxeItem item) {
                    return item.getBaseMiningSpeed();
                }
                if (builder instanceof BasePickaxeComponent component) {
                    return component.getMiningSpeed();
                }
                return 0f;
            },
            (builder, value) -> {
                if (builder instanceof BasePickaxeItem item) {
                    item.setBaseMiningSpeed((Float) value);
                }
                if (builder instanceof BasePickaxeComponent component) {
                    component.setMiningSpeed((Float) value);
                }
            }
    ),
    MINING_FORTUNE(
            Material.DIAMOND,
            Float.class,
            builder -> {
                if (builder instanceof BasePickaxeItem item) {
                    return item.getBaseMiningFortune();
                }
                if (builder instanceof BasePickaxeComponent component) {
                    return component.getMiningFortune();
                }
                return 0f;
            },
            (builder, value) -> {
                if (builder instanceof BasePickaxeItem item) {
                    item.setBaseMiningFortune((Float) value);
                }
                if (builder instanceof BasePickaxeComponent component) {
                    component.setMiningFortune((Float) value);
                }
            }
    ),
    BREAKING_POWER(
            Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
            Integer.class,
            builder -> {
                if (builder instanceof BasePickaxeItem item) {
                    return item.getBreakingPower();
                }
                if (builder instanceof BasePickaxeComponent component) {
                    return component.getBreakingPower();
                }
                return 0f;
            },
            (builder, value) -> {
                if (builder instanceof BasePickaxeItem item) {
                    item.setBreakingPower((int)value);
                }
                if (builder instanceof BasePickaxeComponent component) {
                    component.setBreakingPower((int)value);
                }
            }
    ),
    REQUIRED_BREAKING_POWER(
            Material.ANVIL,
            Integer.class,
            builder -> ((BasePickaxeComponent) builder).getRequiredBreakingPower(),
            (builder, value) -> ((BasePickaxeComponent) builder).setRequiredBreakingPower((int) value)
    ),
    PICKAXE_HEAD(
            Material.IRON_INGOT,
            String.class,
            builder -> ((BasePickaxeItem) builder).getPickaxeHeadId(),
            (builder, value) -> ((BasePickaxeItem) builder).setPickaxeHeadId((String) value)
    ),
    PICKAXE_CORE(
            Material.NETHER_STAR,
            String.class,
            builder -> ((BasePickaxeItem) builder).getPickaxeCoreId(),
            (builder, value) -> ((BasePickaxeItem) builder).setPickaxeCoreId((String) value)
    ),
    PICKAXE_HANDLE(
            Material.STICK,
            String.class,
            builder -> ((BasePickaxeItem) builder).getPickaxeHandleId(),
            (builder, value) -> ((BasePickaxeItem) builder).setPickaxeHandleId((String) value)
    ),
    COMPONENT_ABILITY(
            Material.WIND_CHARGE,
            PickaxeAbilities.class,
            builder -> ((BasePickaxeComponent) builder).getPickaxeAbilityList(),
            (builder, value) -> ((BasePickaxeComponent) builder).changePickaxeAbilityList(((PickaxeAbilities) value).name())
    ),
    FUEL_AMOUNT(
            Material.CHARCOAL,
            Integer.class,
            builder -> ((BaseFuelItem) builder).getFuelAmount(),
            (builder, value) -> ((BaseFuelItem) builder).setFuelAmount((Integer) value)
    ),
    STORAGE_AMOUNT(
            Material.CHEST,
            Integer.class,
            builder -> ((BaseBackpackItem) builder).getStoredItemAmount(),
            (builder, value) -> ((BaseBackpackItem) builder).setStoredItemAmount((Integer) value)
    ),
    STORAGE_LIST(
            Material.PAPER,
            String.class,
            builder -> ((BaseBackpackItem) builder).getStoredItemIdList(),
            (builder, value) -> ((BaseBackpackItem) builder).changeStoredItemIdList((String) value)
    ),
    SELL_VALUE(
            Material.GOLD_INGOT,
            Float.class,
            BaseCustomItem::getValue,
            (builder, value) -> builder.setValue((Float) value)
    ),
    VISUAL_MATERIAL(
            Material.SPYGLASS,
            String.class,
            BaseCustomItem::getVisualMaterial,
            (builder, value) -> builder.setVisualMaterial(value == null ? null : Material.valueOf(((String)value).toUpperCase()))
    ),
    FLAVOR_LORE(
            Material.ITEM_FRAME,
            String.class,
            BaseCustomItem::getFlavorText,
            (builder, value) -> builder.setFlavorText((String)value)
    ),
    GLOWING(
            Material.ENCHANTING_TABLE,
            Boolean.class,
            BaseCustomItem::isGlowing,
            (builder, value) -> builder.setGlowing((Boolean)value)
    ),
    STACK_SIZE(
            Material.HOPPER,
            Integer.class,
            BaseCustomItem::getStackSize,
            (builder, value) -> builder.setStackSize((Integer) value)
    ),
    SOULBOUND(
            Material.SOUL_CAMPFIRE,
            Boolean.class,
            BaseCustomItem::isSoulbound,
            (builder, value) -> builder.setSoulbound((Boolean)value)
    )

    ;

    private final Material material;
    private final Class<?> type;
    private final BiConsumer<BaseCustomItem, Object> setter;
    private final Function<BaseCustomItem, Object> getter;

    ItemOptions(Material material, Class<?> type, Function<BaseCustomItem, Object> getter, BiConsumer<BaseCustomItem, Object> setter) {
        this.type = type;
        this.getter = getter;
        this.setter = setter;
        this.material = material;
    }

    public Class<?> getClassType() {
        return type;
    }

    public void apply(BaseCustomItem item, Object value) {
        setter.accept(item, value);
    }

    public Object get(BaseCustomItem item) {
        return getter.apply(item);
    }

    public boolean isEnum() {
        return type.isEnum();
    }

    public boolean isString() {
        return type == String.class;
    }

    public boolean isFloat() {
        return type == Float.class;
    }

    public boolean isInteger() {
        return type == Integer.class;
    }

    public boolean isList() {
        return type == List.class;
    }

    public boolean isBoolean() {
        return type == Boolean.class;
    }

    public Material getMaterial() {
        return material;
    }
}
