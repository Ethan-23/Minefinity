package org.evasive.me.minefinity.customItems.itembuilder.gui;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.types.BaseCustomItem;
import org.evasive.me.minefinity.core.events.PlayerInputListener;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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
        prompt(raw -> raw, onValue, onDone);
    }

    public void promptString(Consumer<String> onValue) {
        promptString(onValue, this::reopen);
    }

    public void promptInt(Consumer<Integer> onValue, Runnable onDone) {
        prompt(this::parseInt, onValue, onDone);
    }

    public void promptInt(Consumer<Integer> onValue) {
        promptInt(onValue, this::reopen);
    }

    public void promptFloat(Consumer<Float> onValue, Runnable onDone) {
        prompt(this::parseFloat, onValue, onDone);
    }

    public void promptFloat(Consumer<Float> onValue) {
        promptFloat(onValue, this::reopen);
    }

    private <T> void prompt(Function<String, T> parser, Consumer<T> onValue, Runnable onDone) {
        player.closeInventory();
        input.requestInput(player, raw -> {
            if (raw.equalsIgnoreCase("cancel")) {
                onDone.run();
                return;
            }
            T value = parser.apply(raw);
            if (value != null)
                onValue.accept(value);
            onDone.run();
        });
    }

    private Integer parseInt(String raw) {
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            player.sendMessage(TextConversions.parse("<red>That is not a whole number."));
            return null;
        }
    }

    private Float parseFloat(String raw) {
        try {
            float value = Float.parseFloat(raw.trim());
            if (value < 0) {
                player.sendMessage(TextConversions.parse("<red>Number must be positive."));
                return null;
            }
            return value;
        } catch (NumberFormatException e) {
            player.sendMessage(TextConversions.parse("<red>That is not a number."));
            return null;
        }
    }

    public <T> void openSelector(List<T> values, OptionsGui.OptionAdapter<T> adapter, Runnable onApply) {
        new OptionsGui<>(player, values, adapter, onApply).open();
    }

    public <T> void openSelector(List<T> values, OptionsGui.OptionAdapter<T> adapter) {
        openSelector(values, adapter, this::reopen);
    }

    public <T> void openSelector(T[] values, OptionsGui.OptionAdapter<T> adapter) {
        openSelector(List.of(values), adapter);
    }
}
