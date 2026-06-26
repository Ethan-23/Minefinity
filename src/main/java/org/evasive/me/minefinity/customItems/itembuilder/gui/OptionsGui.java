package org.evasive.me.minefinity.customItems.itembuilder.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.gui.GuiUtils;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.itembuilder.events.PlayerInputListener;
import org.evasive.me.minefinity.mining.abilities.PickaxeAbilities;
import org.evasive.me.minefinity.playerdata.stats.data.Stats;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class OptionsGui<T extends Enum<T>> extends BaseGui {

    private final static List<Integer> OPTIONS_SLOTS = List.of(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);
    private final static int INVENTORY_SIZE = 54;
    private final static int BACK_SLOT = 45;
    private final Consumer<Object> onSelect;
    boolean isMap;
    private final T[] enumValues;
    private final ItemCreationGui itemCreationGui;
    private final BaseCustomItem baseCustomItem;
    private final PlayerInputListener playerInputListener;


    protected OptionsGui(Player player, Type type, Consumer<Object> onSelect, BaseCustomItem baseCustomItem, PlayerInputListener playerInputListener, ItemCreationGui itemCreationGui) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Options"));
        this.onSelect = onSelect;
        this.itemCreationGui = itemCreationGui;
        this.playerInputListener = playerInputListener;
        if (type instanceof Class<?> clazz && clazz.isEnum()){
            this.enumValues = getEnumClass(type).getEnumConstants();
        } else if(type instanceof ParameterizedType paramType) {
            this.isMap = true;
            Type keyType = paramType.getActualTypeArguments()[0];
            this.enumValues = getEnumClass(keyType).getEnumConstants();
        } else {
            this.enumValues = null;
        }
        this.baseCustomItem = baseCustomItem;
        build();
    }

    @Override
    protected void build() {
        GuiUtils.fillGui(inventory);
        buildOptions();
        buildBackButton();
    }

    private void buildBackButton(){
        ItemBuilder itemBuilder = new ItemBuilder(Material.ARROW, "Back");
        inventory.setItem(BACK_SLOT, itemBuilder.build());
    }

    private void buildOptions() {
        for (int i = 0; i < enumValues.length; i++) {
            T enumValue = enumValues[i];
            String name = TextConversions.formatItemName(enumValue.name());
            ItemBuilder itemBuilder = new ItemBuilder(Material.BOOK, name);

            if(enumValue instanceof Stats stats){
              name = stats.getDisplay();
              itemBuilder.setDisplayName(name);
              itemBuilder.setMaterial(stats.getMaterial());
              Map<String, Integer> statsMap = baseCustomItem.getStatsMap();
              Integer value = statsMap.get(stats.name());
              itemBuilder.addLore(value != null ? "<white>Value: <yellow>" + value : "<red>Not Selected");
              itemBuilder.addLore("<gray>Left-Click to toggle");
              itemBuilder.addLore("<gray>Right-Click to change value");
              int stackSize = Math.min(value != null && value >= 1 ? value : 1, 64);
              itemBuilder.setStackSize(stackSize);
              itemBuilder.setAmount(stackSize);
              itemBuilder.setGlow(statsMap.containsKey(stats.name()));
            } else if(enumValue instanceof Rarity){
                name = TextConversions.buildRarityColor(name, (Rarity) enumValue);
                itemBuilder.setDisplayName(name);
                itemBuilder.setMaterial(((Rarity) enumValue).getRarityBuilder().material());
                itemBuilder.setGlow((baseCustomItem).getRarity() == enumValue);
            } else if(enumValue instanceof PickaxeAbilities){
                itemBuilder.addLore(((PickaxeAbilities) enumValue).getDescription());
                itemBuilder.setGlow(((BasePartItem)baseCustomItem).getAbilityList().contains(enumValue.name()));
            } else if(enumValue instanceof CustomItemType) {
                itemBuilder.setMaterial(((CustomItemType) enumValue).getDisplayMaterial());
                itemBuilder.setGlow((baseCustomItem).getCustomItemType() == enumValue);
            } else if(enumValue instanceof EquipmentSlot){
                itemBuilder.setGlow((baseCustomItem).getEquipmentSlots().contains((EquipmentSlot) enumValue));
            }

            inventory.setItem(OPTIONS_SLOTS.get(i), itemBuilder.build());
        }
    }

    @SuppressWarnings("unchecked")
    private Class<T> getEnumClass(Type type) {
        if (type instanceof Class<?> clazz && clazz.isEnum()) {
            return (Class<T>) clazz;
        }

        throw new IllegalArgumentException("Type is not an enum: " + type);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);
        e.setCancelled(true);

        int slot = e.getSlot();

        if(slot == BACK_SLOT){
            itemCreationGui.reopen();
            return;
        }

        int index = OPTIONS_SLOTS.indexOf(slot);
        if (index == -1)
            return;

        T[] values = enumValues;
        if (index >= values.length)
            return;

        T selected = values[index];

        if(!isMap){
            onSelect.accept(selected);
            build();
            return;
        }

        if(e.isLeftClick()){
            onSelect.accept(new AbstractMap.SimpleEntry<>(selected, 0));
        }else if(e.isRightClick()){

            player.closeInventory();
            playerInputListener.requestInput(player, input -> {

                if(input.equalsIgnoreCase("cancel")){
                    open();
                    return;
                }

                int number;
                try {
                    number = Integer.parseInt(input);
                }catch (NumberFormatException exception){
                    player.sendMessage(TextConversions.parse("Number is not an integer"));
                    open();
                    return;
                }
                onSelect.accept(new AbstractMap.SimpleEntry<>(selected, number));
                open();
                build();
            });
        }

        build();
    }
}
