package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;
import org.evasive.me.minefinity.customItems.itembuilder.gui.OptionsGui;
import org.evasive.me.minefinity.mining.abilities.PickaxeAbilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.ABILITY_KEY;

public class PartAbilityComponent implements ItemComponent, EditableComponent<List<String>> {

    private List<String> abilities = new ArrayList<>();

    @Override
    public void load(PersistentDataContainer pdc) {
        this.abilities = new ArrayList<>();
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
        for (String ability : abilities) {
            try {
                lore.add(PickaxeAbilities.valueOf(ability).getAbilityDisplay());
            } catch (IllegalArgumentException ignored) {
                // skip unknown abilities from older data
            }
        }
    }

    @Override
    public void setValue(List<String> value) {
        this.abilities = value == null ? new ArrayList<>() : value;
    }

    @Override
    public List<String> getValue() {
        return this.abilities;
    }

    /** Add the ability if absent, remove it if present. */
    public void toggle(String abilityName) {
        if (!abilities.remove(abilityName)) {
            abilities.add(abilityName);
        }
    }

    @Override
    public void openEditor(EditContext ctx) {
        ctx.openSelector(PickaxeAbilities.values(), new OptionsGui.OptionAdapter<>() {
            @Override
            public ItemStack render(PickaxeAbilities ability) {
                ItemBuilder icon = new ItemBuilder(Material.WIND_CHARGE, TextConversions.formatItemName(ability.name()));
                icon.addLore(ability.getDescription());
                icon.addLore("<gray>Click to toggle");
                if (abilities.contains(ability.name())) icon.addGlow();
                return icon.build();
            }

            @Override
            public void onClick(PickaxeAbilities ability, ClickType click, OptionsGui<PickaxeAbilities> gui) {
                toggle(ability.name());
            }
        });
    }
}
