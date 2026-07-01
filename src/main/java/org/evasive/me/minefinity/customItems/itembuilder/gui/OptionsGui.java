package org.evasive.me.minefinity.customItems.itembuilder.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.gui.GuiUtils;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;

import java.util.List;

public class OptionsGui<E extends Enum<E>> extends BaseGui {

    public interface OptionAdapter<E extends Enum<E>> {
        ItemStack render(E value);

        void onClick(E value, ClickType click, OptionsGui<E> gui);
    }

    private static final int INVENTORY_SIZE = 54;
    private static final int APPLY_SLOT = 49;
    private static final List<Integer> OPTION_SLOTS = List.of(
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    );

    private final E[] values;
    private final OptionAdapter<E> adapter;
    private final Runnable onApply;

    public OptionsGui(Player player, E[] values, OptionAdapter<E> adapter, Runnable onApply) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Options"));
        this.values = values;
        this.adapter = adapter;
        this.onApply = onApply;
        build();
    }

    @Override
    protected void build() {
        GuiUtils.fillGui(inventory);
        for (int i = 0; i < values.length && i < OPTION_SLOTS.size(); i++) {
            inventory.setItem(OPTION_SLOTS.get(i), adapter.render(values[i]));
        }
        inventory.setItem(APPLY_SLOT, new CustomItemBuilder(Material.LIME_STAINED_GLASS_PANE, "<green><bold>Apply")
                .addLore("<gray>Apply changes and return")
                .build());
    }

    public void reopenSelf() {
        build();
        open();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);
        e.setCancelled(true);

        int slot = e.getSlot();

        if (slot == APPLY_SLOT) {
            onApply.run();
            return;
        }

        int index = OPTION_SLOTS.indexOf(slot);
        if (index == -1 || index >= values.length) {
            return;
        }

        adapter.onClick(values[index], e.getClick(), this);
        build();
    }
}
