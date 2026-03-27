package org.evasive.me.minefinity.customItems.itembuilder.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.gui.ItemCreationGui;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.events.PlayerInputListener;
import org.evasive.me.minefinity.customItems.registry.config.RegistryConfigHandler;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.ITEM_TYPE_KEY;

public class CreateCustomItem implements CommandExecutor {

    RegistryConfigHandler registryConfigHandler;
    PlayerInputListener playerInputListener;
    private final CustomItemRegistryService customItemRegistryService;

    public CreateCustomItem(CustomItemRegistryService customItemRegistryService, RegistryConfigHandler registryConfigHandler, PlayerInputListener playerInputListener) {
        Objects.requireNonNull(Minefinity.getCore().getCommand("createcustomitem")).setExecutor(this);
        this.registryConfigHandler = registryConfigHandler;
        this.customItemRegistryService = customItemRegistryService;
        this.playerInputListener = playerInputListener;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player))
            return true;

        player.sendMessage(TextConversions.parse("<blue>Opening Item Creator..."));

        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if(heldItem.getType() == Material.AIR){
            player.getInventory().setItemInMainHand(new ItemStack(Material.STICK));
            heldItem = player.getInventory().getItemInMainHand();
        }
        BaseCustomItem item;


        if(heldItem.hasItemMeta() && heldItem.getPersistentDataContainer().has(ITEM_TYPE_KEY)){
            CustomItemType customItemType = CustomItemType.valueOf(heldItem.getPersistentDataContainer().get(ITEM_TYPE_KEY, PersistentDataType.STRING));
            item = customItemType.create(heldItem);
        } else {
            item = new BaseCustomItem(heldItem.getType().name(), heldItem.getType(), TextConversions.formatItemName(heldItem.getType().name()), Rarity.MINOR, CustomItemType.CUSTOM_ITEM);
        }


        new ItemCreationGui(player, item, registryConfigHandler, playerInputListener, customItemRegistryService).open();

        return true;
    }
}
