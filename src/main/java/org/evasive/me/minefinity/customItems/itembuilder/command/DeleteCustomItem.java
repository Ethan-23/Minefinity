package org.evasive.me.minefinity.customItems.itembuilder.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.gui.ConfirmationGui;
import org.evasive.me.minefinity.customItems.itembuilder.data.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.registry.CustomItemRegistry;
import org.evasive.me.minefinity.customItems.itembuilder.registry.config.RegistryConfigHandler;
import org.evasive.me.minefinity.utils.TextConversions;
import org.evasive.me.minefinity.utils.command.CommandFeedback;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DeleteCustomItem implements CommandExecutor {

    RegistryConfigHandler registryConfigHandler;

    public DeleteCustomItem(Minefinity core, RegistryConfigHandler registryConfigHandler) {
        Objects.requireNonNull(core.getCommand("deletecustomitem")).setExecutor(this);
        this.registryConfigHandler = registryConfigHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if(!(sender instanceof Player player)) return true;

        if(args.length != 1){
            player.sendMessage(CommandFeedback.INVALID_USAGE);
            return true;
        }

        String itemId = args[0].toUpperCase();

        if(!CustomItemRegistry.isRegistered(itemId)){
            player.sendMessage(CommandFeedback.INVALID_ITEM_ID);
            return true;
        }

        new ConfirmationGui(player, null, p -> {
            player.sendMessage(CommandFeedback.CUSTOMITEM_DELETE_SUCCESS(itemId));
            registryConfigHandler.removeSingleEntry((BaseCustomItem) CustomItemRegistry.getByID(itemId));
            CustomItemRegistry.removeCustomItem(itemId);
            player.closeInventory();
        }).open();


        return true;
    }
}
