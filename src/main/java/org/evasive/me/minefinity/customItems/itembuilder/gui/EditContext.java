package org.evasive.me.minefinity.customItems.itembuilder.gui;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.core.events.PlayerInputListener;

import java.util.function.Consumer;

public class EditContext {

    private final Player player;
    private final BaseCustomItem item;
    private final PlayerInputListener input;
    private final ItemCreationGui gui;

    public EditContext(Player player, BaseCustomItem item, PlayerInputListener input, ItemCreationGui gui) {
        this.player = player;
        this.item = item;
        this.input = input;
        this.gui = gui;
    }

    public Player player() {
        return player;
    }

    public BaseCustomItem item() {
        return item;
    }

    public void reopen() {
        gui.reopen();
    }

    public void promptString(Consumer<String> onValue, Runnable onDone) {
        player.closeInventory();
        input.requestInput(player, raw -> {
            if (!raw.equalsIgnoreCase("cancel")) {
                onValue.accept(raw);
            }
            onDone.run();
        });
    }

    public void promptString(Consumer<String> onValue) {
        promptString(onValue, this::reopen);
    }

    public void promptInt(Consumer<Integer> onValue, Runnable onDone) {
        player.closeInventory();
        input.requestInput(player, raw -> {
            if (raw.equalsIgnoreCase("cancel")) {
                onDone.run();
                return;
            }
            try {
                onValue.accept(Integer.parseInt(raw.trim()));
            } catch (NumberFormatException e) {
                player.sendMessage(TextConversions.parse("<red>That is not a whole number."));
            }
            onDone.run();
        });
    }

    public void promptInt(Consumer<Integer> onValue) {
        promptInt(onValue, this::reopen);
    }

    public void promptFloat(Consumer<Float> onValue, Runnable onDone) {
        player.closeInventory();
        input.requestInput(player, raw -> {
            if (raw.equalsIgnoreCase("cancel")) {
                onDone.run();
                return;
            }
            try {
                float value = Float.parseFloat(raw.trim());
                if (value < 0) {
                    player.sendMessage(TextConversions.parse("<red>Number must be positive."));
                } else {
                    onValue.accept(value);
                }
            } catch (NumberFormatException e) {
                player.sendMessage(TextConversions.parse("<red>That is not a number."));
            }
            onDone.run();
        });
    }

    public void promptFloat(Consumer<Float> onValue) {
        promptFloat(onValue, this::reopen);
    }

    public <E extends Enum<E>> OptionsGui<E> openSelector(E[] values, OptionsGui.OptionAdapter<E> adapter) {
        OptionsGui<E> selector = new OptionsGui<>(player, values, adapter, this::reopen);
        selector.open();
        return selector;
    }
}
