package org.evasive.me.minefinity.customItems.framework;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCollectItem;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.backpack.BackpackHandler;
import org.evasive.me.minefinity.customItems.backpack.BackpackService;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemPickupService {

    BackpackService backpackService;
    BackpackHandler backpackCollect;
    CustomItemRegistryService customItemRegistryService;

    private final Map<UUID, Long> lastNotification = new HashMap<>();
    private static final long COOLDOWN_MS = 3000;

    public ItemPickupService(CustomItemRegistryService customItemRegistryService, BackpackService backpackService) {
        this.customItemRegistryService = customItemRegistryService;
        this.backpackService = backpackService;
        this.backpackCollect = new BackpackHandler(customItemRegistryService, backpackService);
    }

    public int attemptBackpackStorage(Player player, ItemStack itemStack, int amount){

        String dropId = customItemRegistryService.getItemId(itemStack);

        String backpackId = backpackCollect.findBackpackItem(player, dropId);

        if(backpackId == null || !backpackCollect.canHoldItem(player, backpackId, dropId)){
            return amount;
        }

        return addToBackpack(player, dropId, backpackId, amount);
    }

    private int addToBackpack(Player player, String itemId, String backpackId, int amount){

        int total = backpackService.getBackpackStoredItemAmount(player, itemId);
        final int max = backpackCollect.getTotalBackpackStorage(player, backpackId);

        int space = max - total;

        if(space == 0)
            return amount;

        if(space >= amount){
            backpackService.addBackpackItem(player, itemId, amount);
            return 0;
        }

        backpackService.addBackpackItem(player, itemId, space);
        return amount - space;

    }

    public void fullInventoryNotification(Player player){

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

    public int attemptInventoryStorage(Player player, ItemStack stack, int remaining) {
        ItemStack remainingStack = stack.clone();
        remainingStack.setAmount(remaining);

        Map<Integer, ItemStack> leftover = player.getInventory().addItem(remainingStack);

        return leftover.values().stream().mapToInt(ItemStack::getAmount).sum();
    }

    public int givePlayerDrops(Player player, ItemStack drop, int amount){
        int overflow = attemptBackpackStorage(player, drop, amount);

        if(overflow > 0)
            overflow = attemptInventoryStorage(player, drop, amount);

        if(overflow > 0){
            fullInventoryNotification(player);
            return overflow;
        }
        return 0;
    }

}
