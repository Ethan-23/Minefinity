package org.evasive.me.minefinity.customItems.framework;

import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.backpack.BackpackHandler;
import org.evasive.me.minefinity.customItems.itembuilder.registry.CustomItemRegistry;
import org.evasive.me.minefinity.utils.TextConversions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.getItemId;

public class ItemGiver {

    BackpackHandler backpackCollect = new BackpackHandler();

    private final Map<UUID, Long> lastNotification = new HashMap<>();
    private static final long COOLDOWN_MS = 3000;

    //Need to rework this entire system to go off itemStacks because if id doesn't exist it dies

    public int givePlayerDrops(Player player, ItemStack itemStack, int amount){

        String dropId = getItemId(itemStack);

        String backpackId = backpackCollect.findBackpackItem(player, dropId);

        if(player.getInventory().firstEmpty() == -1 && (backpackId == null || !backpackCollect.canHoldItem(player, backpackId, dropId))){
            fullInventoryNotification(player);
            return amount;
        }

        if (backpackId != null)
            amount = addToBackpack(player, dropId, backpackId, amount);

        amount = giveMultipleItems(player, itemStack, amount);

        if(amount > 0)
            fullInventoryNotification(player);

        return amount;
    }

    public int givePlayerDrops(Player player, String dropId, int amount){

        String backpackId = backpackCollect.findBackpackItem(player, dropId);

        if(player.getInventory().firstEmpty() == -1 && (backpackId == null || !backpackCollect.canHoldItem(player, backpackId, dropId))){
            fullInventoryNotification(player);
            return amount;
        }

        if (backpackId != null)
            amount = addToBackpack(player, dropId, backpackId, amount);

        if(CustomItemRegistry.isRegistered(dropId))
            amount = giveMultipleItems(player, CustomItemRegistry.getByID(dropId).getBaseItem().buildItem(), amount);

        if(amount > 0)
            fullInventoryNotification(player);

        return amount;
    }

    private int addToBackpack(Player player, String itemId, String backpackId, int amount){

        int total = Minefinity.getCore().getBackpackService().getBackpackStoredItemAmount(player, itemId);
        final int max = backpackCollect.getTotalBackpackStorage(player, backpackId);

        int space = max - total;

        if(space == 0)
            return amount;

        if(space >= amount){
            Minefinity.getCore().getBackpackService().addBackpackItem(player, itemId, amount);
            return 0;
        }

        Minefinity.getCore().getBackpackService().addBackpackItem(player, itemId, space);
        return amount - space;

    }

    private int giveMultipleItems(Player player, ItemStack itemStack, int total){
        String itemId = getItemId(itemStack);
        if(!CustomItemRegistry.isRegistered(itemId)){
            Bukkit.getConsoleSender().sendMessage("Unregistered Item Id: " + itemId);
            player.getInventory().addItem(itemStack);
            return 0;
        }

        ItemStack customItem = CustomItemRegistry.getByID(itemId).getBaseItem().buildItem();

        final int MAX_STACK_SIZE = customItem.getMaxStackSize();

        while (total > 0 && player.getInventory().firstEmpty() != -1) {
            int giveAmount = Math.min(total, MAX_STACK_SIZE);

            customItem.setAmount(giveAmount);
            player.getInventory().addItem(customItem);
            total -= giveAmount;
        }
        return total;
    }

    private void fullInventoryNotification(Player player){

        long now = System.currentTimeMillis();
        UUID uuid = player.getUniqueId();

        if(lastNotification.containsKey(uuid)){
            long lastSent = lastNotification.get(uuid);

            if(now - lastSent < COOLDOWN_MS)return;
        }

        lastNotification.put(uuid, now);

        player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
        player.showTitle(Title.title(TextConversions.parse("<red>INVENTORY FULL!!!"), TextConversions.parse("")));
    }

}
