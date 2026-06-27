package org.evasive.me.minefinity.customItems.itembuilder.gui;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.events.PlayerInputListener;

import java.util.function.Consumer;

/**
 * Everything a component needs to edit itself: the player, the item being edited,
 * the chat-input listener and a way back to the parent creation GUI.
 *
 * <p>Components receive this in {@code EditableComponent#openEditor} so the editing logic
 * lives with the component instead of in one central switch statement.</p>
 */
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

    /** Re-render and re-open the parent item creation GUI. */
    public void reopen() {
        gui.reopen();
    }

    /**
     * Prompt the player for free text in chat. {@code cancel} aborts.
     * Always returns to {@code onDone} (defaults to reopening the item GUI).
     */
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

    /** Prompt for an integer; re-prompts on bad input, {@code cancel} aborts. */
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

    /** Prompt for a non-negative float; re-prompts on bad input, {@code cancel} aborts. */
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

    /** Open a generic enum selector, returning to this item GUI when the player backs out. */
    public <E extends Enum<E>> OptionsGui<E> openSelector(E[] values, OptionsGui.OptionAdapter<E> adapter) {
        OptionsGui<E> selector = new OptionsGui<>(player, values, adapter, this::reopen);
        selector.open();
        return selector;
    }
}
