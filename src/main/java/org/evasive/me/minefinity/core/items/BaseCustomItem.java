package org.evasive.me.minefinity.core.items;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.items.CustomItemType;
import org.evasive.me.minefinity.rarity.Rarity;
import org.evasive.me.minefinity.utils.ItemBuilder;
import org.evasive.me.minefinity.utils.Messages;

import java.util.List;

import static org.evasive.me.minefinity.customItems.ItemFunctions.itemIDKey;
import static org.evasive.me.minefinity.customItems.ItemNameBuilder.*;

public class BaseCustomItem implements CustomItem {

    protected ItemStack cachedStack;

    private final String id;
    private final Material material;
    private final Rarity rarity;
    private final CustomItemType itemType;
    private final float value;

    public BaseCustomItem(String id, Material material, Rarity rarity, CustomItemType itemType, float value) {
        this.id = id;
        this.material = material;
        this.rarity = rarity;
        this.itemType = itemType;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public CustomItemType getItemType() {
        return itemType;
    }

    public float getValue() {
        return value;
    }

    protected Component getName() {
        return Messages.parse(buildRarityColor(getId(), getRarity()));
    }

    protected List<String> getLore() {
        return List.of(
                buildItemType(getItemType().name()),
                "",
                buildItemRarity(getRarity())
        );
    }

    public ItemStack buildItem() {
        if(cachedStack == null) {
            cachedStack = new ItemBuilder(getMaterial(), getName())
                    .addLore(getLore())
                    .addPersistentDataContainer(itemIDKey, id)
                    .build();
        }
        return cachedStack.clone();
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public BaseCustomItem getBuilder() {
        return this;
    }
}
