package org.evasive.me.minefinity.customItems.pickaxe;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.items.BaseCustomItem;
import org.evasive.me.minefinity.customItems.items.CustomItemType;
import org.evasive.me.minefinity.rarity.Rarity;
import org.evasive.me.minefinity.utils.ItemBuilder;
import org.evasive.me.minefinity.utils.Messages;

import java.util.List;
import java.util.Objects;

import static org.evasive.me.minefinity.customItems.ItemFunctions.*;
import static org.evasive.me.minefinity.customItems.ItemNameBuilder.buildItemRarity;
import static org.evasive.me.minefinity.customItems.ItemNameBuilder.formatItemName;

public class BasePickaxeItem extends BaseCustomItem {

    private final float baseMiningSpeed;

    public BasePickaxeItem(String id, Material material, Rarity rarity, CustomItemType itemType, float baseMiningSpeed) {
        super(id, material, rarity, itemType, -1);
        this.baseMiningSpeed = baseMiningSpeed;
    }

    public float getBaseMiningSpeed() {
        return baseMiningSpeed;
    }

    @Override
    protected Component getName() {
        return Messages.parse("<gray>Pickaxe");
    }

    @Override
    protected List<String> getLore() {
        return List.of(
                "<gray>Mining Speed: <white>"+baseMiningSpeed,
                "",
                "<gray>Components:",
                "<gray>[]",
                "<gray>[]",
                "<gray>[]",
                "",
                buildItemRarity(getRarity())
        );
    }

    public ItemStack createBasePickaxe(){
        return new ItemBuilder(getMaterial(), getName())
                .addPersistentDataContainer(itemIDKey, getID())
                .addPersistentDataContainer(headKey, "NONE")
                .addPersistentDataContainer(coreKey, "NONE")
                .addPersistentDataContainer(handleKey, "NONE")
                .addLore(getLore())
                .build();
    }

    public ItemStack rebuildPickaxe(ItemStack itemStack){
        ItemBuilder builder = new ItemBuilder(itemStack);

        String headId = getStringPDC(itemStack, headKey);
        String coreId = getStringPDC(itemStack,  coreKey);
        String handleId = getStringPDC(itemStack, handleKey);

        if(!Objects.equals(headId, "NONE")){
            String color = PickaxeComponent.valueOf(headId).getBuilder().getColorCode();
            builder.setDisplayName(Messages.parse("<color:<color>><type> Pickaxe", Placeholder.parsed("color", color), Placeholder.parsed("type", formatItemName(headId).split(" ")[0])));
        }

        float totalSpeed = baseMiningSpeed;

        List<Component> lore = List.of(
                Messages.parse("<gray>Mining Speed: <white><speed>",
                        Placeholder.parsed("speed", String.format("%.2f", totalSpeed))),
                Messages.parse(""),
                Messages.parse("<gray>Components:"),
                componentLine(headId),
                componentLine(coreId),
                componentLine(handleId),
                Messages.parse(""),
                Messages.parse("<bold><gray>" + formatItemName(getID()))
                //buildItemRarity(getRarity())
        );

        builder.setLore(lore);
        return builder.build();
    }

    private Component componentLine(String value) {
        String part = value.equals("NONE") ? "Empty" : value;
        String color = value.equals("NONE") ? "gray" : PickaxeComponent.valueOf(part.toUpperCase()).getBuilder().getColorCode();

        return Messages.parse("<gray>[<color:<pcolor>><part><gray>]",
                Placeholder.parsed("part", formatItemName(part)),
                Placeholder.parsed("pcolor", color)
        );
    }

    public ItemStack addPart(ItemStack itemStack, NamespacedKey namespacedKey, String partId){
        return rebuildPickaxe(new ItemBuilder(itemStack).addPersistentDataContainer(namespacedKey, partId).build());
    }

    public ItemStack removePart(ItemStack itemStack, NamespacedKey namespacedKey){
        return rebuildPickaxe(new ItemBuilder(itemStack).addPersistentDataContainer(namespacedKey, "NONE").build());
    }

    @Override
    public ItemStack buildItem() {
        // Optional: Add custom enchantments, attributes, or NBT
        return rebuildPickaxe(createBasePickaxe());
    }
}
