package org.evasive.me.minefinity.customItems.pickaxe;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.items.BaseCustomItem;
import org.evasive.me.minefinity.customItems.framework.CustomItemRegistry;
import org.evasive.me.minefinity.customItems.types.CustomItemType;
import org.evasive.me.minefinity.rarity.Rarity;
import org.evasive.me.minefinity.utils.ItemBuilder;
import org.evasive.me.minefinity.utils.TextConversions;

import java.util.List;

import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.*;
import static org.evasive.me.minefinity.utils.TextConversions.*;

public class BasePickaxeItem extends BaseCustomItem {

    private final float baseMiningSpeed;
    private PickaxeComponent pickaxeHead;
    private PickaxeComponent pickaxeCore;
    private PickaxeComponent pickaxeHandle;

    public static final NamespacedKey headKey = new NamespacedKey(Minefinity.getCore(), "Head");
    public static final NamespacedKey coreKey = new NamespacedKey(Minefinity.getCore(), "Core");
    public static final NamespacedKey handleKey = new NamespacedKey(Minefinity.getCore(), "Handle");

    public BasePickaxeItem(String id, Material material, Rarity rarity, CustomItemType itemType, float baseMiningSpeed, PickaxeComponent pickaxeHead, PickaxeComponent pickaxeCore, PickaxeComponent pickaxeHandle) {
        super(id, material, rarity, itemType, -1);
        this.baseMiningSpeed = baseMiningSpeed;
        this.pickaxeHead = pickaxeHead;
        this.pickaxeCore = pickaxeCore;
        this.pickaxeHandle = pickaxeHandle;
    }

    public BasePickaxeItem(ItemStack pickaxeItem){
        super(getItemId(pickaxeItem), pickaxeItem.getType(), CustomItemRegistry.getByID(getItemId(pickaxeItem)).getBuilder().getRarity(), CustomItemRegistry.getByID(getItemId(pickaxeItem)).getBuilder().getItemType(), -1);
        this.baseMiningSpeed = ((BasePickaxeItem) CustomItemRegistry.getByID(getItemId(pickaxeItem)).getBuilder()).baseMiningSpeed;
        String pickaxeHeadId = getStringPDC(pickaxeItem, headKey);
        this.pickaxeHead = PickaxeComponent.contains(pickaxeHeadId) ? PickaxeComponent.valueOf(pickaxeHeadId) : null;
        String pickaxeCoreId = getStringPDC(pickaxeItem, coreKey);
        this.pickaxeCore = PickaxeComponent.contains(pickaxeCoreId) ? PickaxeComponent.valueOf(pickaxeCoreId) : null;
        String pickaxeHandleId = getStringPDC(pickaxeItem, handleKey);
        this.pickaxeHandle = PickaxeComponent.contains(pickaxeHandleId) ? PickaxeComponent.valueOf(pickaxeHandleId) : null;
    }


    public float getBaseMiningSpeed() {
        return baseMiningSpeed;
    }

    @Override
    protected Component getName() {

        if(this.pickaxeHead == null) {
            return TextConversions.parse(TextConversions.buildRarityColor(this.getID().replace("TEMPLATE", "PICKAXE"), this.getRarity()));
        } else {
            return TextConversions.parse(TextConversions.buildRarityColor(pickaxeHead.getID().replace("HEAD", "PICKAXE"), this.getRarity()));
        }
    }

    @Override
    protected List<String> getLore() {

        return List.of(
                "<gray>Mining Speed: <white>" + String.format("%.2f", this.baseMiningSpeed),
                "",
                "<gray>Components:",
                "<gray>[" + (pickaxeHead == null ? buildRarityColor(getID().replace("TEMPLATE", "HEAD"), getRarity()) : TextConversions.buildColor(pickaxeHead.getID(), pickaxeHead.getBuilder().getColorCode())) + "<gray>]",
                "<gray>[" + (pickaxeCore == null ? buildRarityColor(getID().replace("TEMPLATE", "CORE"), getRarity()) : TextConversions.buildColor(pickaxeCore.getID(), pickaxeCore.getBuilder().getColorCode())) + "<gray>]",
                "<gray>[" + (pickaxeHandle == null ? buildRarityColor(getID().replace("TEMPLATE", "HANDLE"), getRarity()) : TextConversions.buildColor(pickaxeHandle.getID(), pickaxeHandle.getBuilder().getColorCode())) + "<gray>]",
                "",
                buildRarityColor(this.getID(), getRarity()),
                buildItemRarity(getRarity())
        );
    }

    public ItemStack createBasePickaxe(){
        return new ItemBuilder(getMaterial(), getName())
                .addPersistentDataContainer(itemIDKey, getID())
                .addPersistentDataContainer(headKey, pickaxeHead == null? "NONE" : pickaxeHead.getID())
                .addPersistentDataContainer(coreKey, pickaxeCore == null? "NONE" : pickaxeCore.getID())
                .addPersistentDataContainer(handleKey, pickaxeHandle == null? "NONE" : pickaxeHandle.getID())
                .addLore(getLore())
                .addUnbreakable()
                .build();
    }

    public ItemStack rebuildPickaxe(ItemStack itemStack){
        ItemBuilder builder = new ItemBuilder(itemStack);

        builder.setDisplayName(getName());

        builder.setLore(getLore());

        return builder.build();
    }

    public BasePickaxeItem setPickaxeHead(PickaxeComponent pickaxeComponent){
        this.pickaxeHead = pickaxeComponent;
        return this;
    }

    public BasePickaxeItem setPickaxeCore(PickaxeComponent pickaxeComponent){
        this.pickaxeCore = pickaxeComponent;
        return this;
    }

    public BasePickaxeItem setPickaxeHandle(PickaxeComponent pickaxeComponent){
        this.pickaxeHandle = pickaxeComponent;
        return this;
    }

    @Override
    public ItemStack buildItem() {
        // Optional: Add custom enchantments, attributes, or NBT
        return rebuildPickaxe(createBasePickaxe());
    }
}
