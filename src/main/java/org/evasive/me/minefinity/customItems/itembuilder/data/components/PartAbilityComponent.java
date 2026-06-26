package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.mining.abilities.PickaxeAbilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.ABILITY_KEY;

public class PartAbilityComponent implements ItemComponent, EditableComponent<List<String>> {

    List<String> abilities;

    @Override
    public void load(PersistentDataContainer pdc) {
        String abilityData = pdc.get(ABILITY_KEY, PersistentDataType.STRING);

        if (abilityData != null && !abilityData.isEmpty()) {

            this.abilities = new ArrayList<>(Arrays.asList(abilityData.split(";;")));
        }
    }

    @Override
    public void save(ItemBuilder builder) {
        builder.addPersistentDataContainer(ABILITY_KEY, PersistentDataType.STRING, String.join(";;", abilities));
    }

    @Override
    public void addLore(List<String> lore) {

    }

    @Override
    public Class<?> type() {
        return PickaxeAbilities.class;
    }

    @Override
    public void setValue(List<String> value) {
        this.abilities = value;
    }

    @Override
    public List<String> getValue() {
        return this.abilities;
    }


}
