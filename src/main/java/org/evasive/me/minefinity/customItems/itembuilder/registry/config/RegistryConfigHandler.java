package org.evasive.me.minefinity.customItems.itembuilder.registry.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.evasive.me.minefinity.customItems.itembuilder.data.*;
import org.evasive.me.minefinity.customItems.itembuilder.registry.CustomItemRegistry;

import java.util.Optional;

public class RegistryConfigHandler {

    private final ItemRegistryConfigManager itemRegistryConfigManager;

    public RegistryConfigHandler(ItemRegistryConfigManager itemRegistryConfigManager) {
        this.itemRegistryConfigManager = itemRegistryConfigManager;
    }

    public void saveEntireRegistry(){
        CustomItemRegistry.getAllItems().forEach(item -> {
            addSingleEntry((BaseCustomItem) item);
        });
        itemRegistryConfigManager.saveItemRegistryConfig();
    }

    public void addSingleEntry(BaseCustomItem item){
        ConfigurationSection individualItemSection = itemRegistryConfigManager.getItemConfigSection().createSection(item.getID());

        CustomItemType customItemType = item.getCustomItemType();

        individualItemSection.set("material", item.getMaterial().name());
        individualItemSection.set("display_name", item.getDisplayName());
        individualItemSection.set("custom_item_type", customItemType.name());
        individualItemSection.set("rarity", item.getRarity().name());

        Optional<Float> value = item.getValue();
        value.ifPresent(aFloat -> individualItemSection.set("sell_value", aFloat));
        Optional<Material> visualMaterial = item.getVisualMaterial();
        visualMaterial.ifPresent(aMaterial -> individualItemSection.set("visual_material", aMaterial.name()));
        Optional<String> flavorText = item.getFlavorText();
        flavorText.ifPresent(aFlavor -> individualItemSection.set("flavor_text", aFlavor));
        Optional<Boolean> glowing = item.isGlowing();
        glowing.ifPresent(aGlowing -> individualItemSection.set("glowing", aGlowing));
        Optional<Boolean> soulbound = item.isSoulbound();
        soulbound.ifPresent(aSoulbound -> individualItemSection.set("soulbound", aSoulbound));
        Optional<Integer> stackSize = item.getStackSize();
        stackSize.ifPresent(aStackSize -> individualItemSection.set("stack_size", aStackSize));



        switch (item) {
            case BasePickaxeItem basePickaxeItem -> {
                individualItemSection.set("mining_speed", basePickaxeItem.getBaseMiningSpeed());
                individualItemSection.set("pickaxe_head", basePickaxeItem.getPickaxeHeadId());
                individualItemSection.set("pickaxe_core", basePickaxeItem.getPickaxeCoreId());
                individualItemSection.set("pickaxe_handle", basePickaxeItem.getPickaxeHandleId());
            }
            case BasePickaxeComponent basePickaxeComponent ->
                    individualItemSection.set("pickaxe_abilities", basePickaxeComponent.getPickaxeAbilityList());
            case BaseFuelItem baseFuelItem ->
                    individualItemSection.set("fuel_amount", baseFuelItem.getFuelAmount());
            case BaseBackpackItem baseBackpackItem -> {
                individualItemSection.set("storage_amount", baseBackpackItem.getStoredItemAmount());
                individualItemSection.set("storage_list", baseBackpackItem.getStoredItemIdList());
            }
            default -> {
            }
        }

        itemRegistryConfigManager.saveItemRegistryConfig();
    }

    public void removeSingleEntry(BaseCustomItem item){
        if(!itemRegistryConfigManager.getItemConfigSection().isConfigurationSection(item.getID()))
            return;
        itemRegistryConfigManager.getItemConfigSection().set(item.getID(), null);
        itemRegistryConfigManager.saveItemRegistryConfig();
    }

}
