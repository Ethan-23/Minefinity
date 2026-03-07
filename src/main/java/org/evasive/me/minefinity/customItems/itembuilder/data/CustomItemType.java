package org.evasive.me.minefinity.customItems.itembuilder.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.rarity.Rarity;

import java.util.ArrayList;
import java.util.List;

public enum CustomItemType {
    CUSTOM_ITEM(
            BaseCustomItem::new, BaseCustomItem::new,
            BaseCustomItem.getRequiredOptions(),
            BaseCustomItem.getOptionalOptions(),
            Material.COMMAND_BLOCK
    ),
    RESOURCE(BaseResourceItem::new,BaseResourceItem::new,
            BaseResourceItem.getRequiredOptions(),
            BaseResourceItem.getOptionalOptions(),
            Material.COPPER_ORE
    ),
    PICKAXE(BasePickaxeItem::new, BasePickaxeItem::new,
            BasePickaxeItem.getRequiredOptions(),
            BasePickaxeItem.getOptionalOptions(),
            Material.WOODEN_PICKAXE
    ),
    PICKAXE_HEAD(BasePickaxeComponent::new, BasePickaxeComponent::new,
            BasePickaxeComponent.getRequiredOptions(),
            BasePickaxeComponent.getOptionalOptions(),
            Material.IRON_INGOT
            ),
    PICKAXE_CORE(BasePickaxeComponent::new, BasePickaxeComponent::new,
            BasePickaxeComponent.getRequiredOptions(),
            BasePickaxeComponent.getOptionalOptions(),
            Material.NETHER_STAR
    ),
    PICKAXE_HANDLE(BasePickaxeComponent::new, BasePickaxeComponent::new,
            BasePickaxeComponent.getRequiredOptions(),
            BasePickaxeComponent.getOptionalOptions(),
            Material.STICK
    ),
    FUEL(BaseFuelItem::new,BaseFuelItem::new,
            BaseFuelItem.getRequiredOptions(),
            BaseFuelItem.getOptionalOptions(),
            Material.CHARCOAL
    ),
    STORAGE(BaseBackpackItem::new,BaseBackpackItem::new,
            BaseBackpackItem.getRequiredOptions(),
            BaseBackpackItem.getOptionalOptions(),
            Material.CHEST
    )
    ;

    private final NewItemFactory newItemFactory;
    private final ItemStackFactory itemStackFactory;
    private final List<ItemOptions> requiredOptions;
    private final List<ItemOptions> optionalOptions;
    private final Material displayMaterial;

    CustomItemType(NewItemFactory newItemFactory, ItemStackFactory itemStackFactory, List<ItemOptions> requiredOptions, List<ItemOptions> optionalOptions, Material displayMaterial) {
        this.newItemFactory = newItemFactory;
        this.itemStackFactory = itemStackFactory;
        this.requiredOptions = requiredOptions;
        this.optionalOptions = optionalOptions;
        this.displayMaterial = displayMaterial;
    }

    public BaseCustomItem create(String id, Material material, String name, Rarity rarity) {
        return newItemFactory.create(id, material, name, rarity);
    }

    public BaseCustomItem create(ItemStack itemStack) {
        return itemStackFactory.create(itemStack);
    }

    public List<ItemOptions> getRequiredOptions() {
        return requiredOptions;
    }

    public List<ItemOptions> getOptionalOptions() {
        return optionalOptions;
    }

    public Material getDisplayMaterial() {
        return displayMaterial;
    }

    public List<ItemOptions> getAllOptions(){
        List<ItemOptions> list = new ArrayList<>(requiredOptions);
        list.addAll(optionalOptions);
        return list;
    }
}
