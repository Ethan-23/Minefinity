package org.evasive.me.minefinity.customItems.itembuilder.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.BasePickaxeComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.mining.abilities.PickaxeAbilities;
import org.evasive.me.minefinity.rarity.Rarity;
import org.evasive.me.minefinity.utils.TextConversions;

import java.util.List;
import java.util.function.Consumer;

import static org.evasive.me.minefinity.utils.GenericGuiItems.fillerPane;

public class OptionsGui<T extends Enum<T>> extends BaseGui {

    private final static List<Integer> OPTIONS_SLOTS = List.of(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);
    private final static int INVENTORY_SIZE = 54;
    private final Class<T> classType;
    private final Consumer<T> onSelect;
    private final T[] enumValues;
    private final BaseCustomItem baseCustomItem;


    protected OptionsGui(Player player, Class<T> classType, Consumer<T> onSelect, BaseCustomItem baseCustomItem) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Options"));
        this.classType = classType;
        this.onSelect = onSelect;
        this.enumValues = classType.getEnumConstants();
        this.baseCustomItem = baseCustomItem;
        build();
    }

    @Override
    protected void build() {
        fillGui();
        buildOptions();
    }

    private void fillGui(){
        for(int i = 0; i < INVENTORY_SIZE; i++){
            inventory.setItem(i, fillerPane);
        }
    }

    private void buildOptions() {
        for (int i = 0; i < enumValues.length; i++) {
            T enumValue = enumValues[i];
            String name = TextConversions.formatItemName(enumValue.name());
            ItemBuilder itemBuilder = new ItemBuilder(Material.BOOK, name);

            if(enumValue instanceof Rarity){
                name = TextConversions.buildRarityColor(name, (Rarity) enumValue);
                itemBuilder.setDisplayName(name);
                itemBuilder.setMaterial(((Rarity) enumValue).getRarityBuilder().getMaterial());
                itemBuilder.setGlow((baseCustomItem).getRarity() == enumValue);
            } else if(enumValue instanceof PickaxeAbilities){
                itemBuilder.addLore(((PickaxeAbilities) enumValue).getDescription());
                itemBuilder.setGlow(((BasePickaxeComponent)baseCustomItem).getPickaxeAbilityList().contains(enumValue));
            } else if(enumValue instanceof CustomItemType) {
                itemBuilder.setMaterial(((CustomItemType) enumValue).getDisplayMaterial());
                itemBuilder.setGlow((baseCustomItem).getCustomItemType() == enumValue);
            }

            inventory.setItem(OPTIONS_SLOTS.get(i), itemBuilder.build());
        }
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);
        e.setCancelled(true);

        int slot = e.getSlot();
        int index = OPTIONS_SLOTS.indexOf(slot);
        if (index == -1)
            return;

        T[] values = classType.getEnumConstants();
        if (index >= values.length)
            return;

        T selected = values[index];
        onSelect.accept(selected);
    }
}
