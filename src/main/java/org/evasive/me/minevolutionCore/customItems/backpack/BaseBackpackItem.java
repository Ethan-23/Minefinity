package org.evasive.me.minevolutionCore.customItems.backpack;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.core.items.BaseCustomItem;
import org.evasive.me.minevolutionCore.customItems.items.CustomItemType;
import org.evasive.me.minevolutionCore.rarity.Rarity;
import org.evasive.me.minevolutionCore.utils.ItemBuilder;
import org.evasive.me.minevolutionCore.utils.Messages;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.evasive.me.minevolutionCore.customItems.ItemNameBuilder.buildItemRarity;
import static org.evasive.me.minevolutionCore.customItems.ItemNameBuilder.formatItemName;

public class BaseBackpackItem extends BaseCustomItem {

    private final int storedItemAmount;
    private final Set<String> storedItemIdList;
    private final Material visual;

    public BaseBackpackItem(String id, Material material, Material visual, Rarity rarity, CustomItemType itemType, int storedItemAmount, Set<String> storedItemIdList) {
        super(id, material, rarity, itemType, -1);
        this.storedItemAmount = storedItemAmount;
        this.storedItemIdList = storedItemIdList;
        this.visual = visual;
    }

    @Override
    protected Component getName() {
        return Messages.parse(formatItemName(getID())).color(getRarity().getRarityBuilder().getTextColor());
    }

    //Update lore to take multiple lines if too long
    @Override
    protected List<String> getLore() {
        return List.of(
                "<gray>Item pickups go directly into your backpack",
                "",
                "<gray>Items: "+ getStoredItemIdList().stream().map(string -> "<green>" + formatItemName(string)).sorted().collect(Collectors.joining("<gray>, ")),
                "",
                "<gray>Capacity <yellow>" + getStoredItemAmount() + " of each held item",
                "",
                "<yellow>Right Click to open backpack!",
                "",
                buildItemRarity(getRarity())
        );
    }

    @Override
    public ItemStack buildItem() {
        return new ItemBuilder(super.buildItem()).setItemModel(visual.getKey()).addUniqueTag().build();
    }

    public int getStoredItemAmount() {
        return storedItemAmount;
    }

    public Set<String> getStoredItemIdList() {
        return Collections.unmodifiableSet(storedItemIdList);
    }
}
