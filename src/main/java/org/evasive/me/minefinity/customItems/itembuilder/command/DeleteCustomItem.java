package org.evasive.me.minefinity.customItems.itembuilder.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.gui.ConfirmationGui;
import org.evasive.me.minefinity.customItems.registry.config.RegistryConfigHandler;
import org.evasive.me.minefinity.core.utils.command.CommandFeedback;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DeleteCustomItem implements CommandExecutor {

    RegistryConfigHandler registryConfigHandler;
    private final CustomItemRegistryService customItemRegistryService;

    public DeleteCustomItem(CustomItemRegistryService customItemRegistryService, RegistryConfigHandler registryConfigHandler) {
        Objects.requireNonNull(Minefinity.getCore().getCommand("deletecustomitem")).setExecutor(this);
        this.customItemRegistryService = customItemRegistryService;
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

        if(!customItemRegistryService.isRegistered(itemId)){
            player.sendMessage(CommandFeedback.INVALID_ITEM_ID);
            return true;
        }

        new ConfirmationGui(player, null, p -> {
            player.sendMessage(CommandFeedback.CUSTOMITEM_DELETE_SUCCESS(itemId));
            registryConfigHandler.removeSingleEntry(customItemRegistryService.getRegisteredBaseItem(itemId));
            customItemRegistryService.removeRegisteredItem(itemId);
            player.closeInventory();
        }).open();


        return true;
    }
}
