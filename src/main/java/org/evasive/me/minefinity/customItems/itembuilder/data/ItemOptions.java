package org.evasive.me.minefinity.customItems.itembuilder.data;

import com.google.common.reflect.TypeToken;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.*;
import org.evasive.me.minefinity.mining.abilities.PickaxeAbilities;
import org.evasive.me.minefinity.playerdata.stats.data.Stats;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
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
    STATS(
            Material.GRINDSTONE,
            new TypeToken<Map<Stats, Integer>>() {}.getType(),
            BaseCustomItem::getStatsMap,
            (builder, value) -> {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) value;
                builder.addStatsMap((Stats) entry.getKey(), (Integer) entry.getValue());
            }
    ),
    EQUIPMENT_SLOT(
            Material.NETHERITE_HELMET,
            EquipmentSlot.class,
            BaseCustomItem::getEquipmentSlots,
            (builder, value) -> (builder).changeEquipmentList((EquipmentSlot) value)
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
    private final Type type;
    private final BiConsumer<BaseCustomItem, Object> setter;
    private final Function<BaseCustomItem, Object> getter;

    ItemOptions(Material material, Type type, Function<BaseCustomItem, Object> getter, BiConsumer<BaseCustomItem, Object> setter) {
        this.type = type;
        this.getter = getter;
        this.setter = setter;
        this.material = material;
    }

    public Type getClassType() {
        return type;
    }

    public void apply(BaseCustomItem item, Object value) {
        setter.accept(item, value);
    }

    public Object get(BaseCustomItem item) {
        return getter.apply(item);
    }

    public boolean isEnum() {
        return type instanceof Class<?> clazz && clazz.isEnum();
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

    public boolean isMap() {
        return type instanceof ParameterizedType paramType
                && paramType.getRawType() instanceof Class<?> clazz
                && Map.class.isAssignableFrom(clazz);
    }

    public Material getMaterial() {
        return material;
    }
}
