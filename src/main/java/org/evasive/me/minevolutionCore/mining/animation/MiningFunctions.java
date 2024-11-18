package org.evasive.me.minevolutionCore.mining.animation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.mining.blocks.BlockDataFunctions;
import org.evasive.me.minevolutionCore.player.PlayerManager;
import org.evasive.me.minevolutionCore.utils.ComponentUtils;
import org.evasive.me.minevolutionCore.utils.EnchantKeys;
import org.evasive.me.minevolutionCore.utils.PickaxeKeys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.evasive.me.minevolutionCore.utils.ComponentUtils.makeText;

public class MiningFunctions {

    private Map<UUID, Integer> superbreakerUsers = new HashMap<>();
    BlockDataFunctions blockDataFunctions = new BlockDataFunctions();

    public void mainBlockMined(Player player, Block block){
        PlayerManager playerManager = MinevolutionCore.getPlayerManager();
        Material material = blockDataFunctions.getMaterialDrop(player);

        //Player cannot move pickaxe out of first slot
        ItemStack pickaxe = player.getInventory().getItemInMainHand();

        //Check all enchants
        assert pickaxe != null;

        boolean compact = false;
        if(pickaxe.getPersistentDataContainer().has(EnchantKeys.compact))
            compact = compactCheck(player, pickaxe.getPersistentDataContainer().get(EnchantKeys.compact, PersistentDataType.INTEGER));
        if(pickaxe.getPersistentDataContainer().has(EnchantKeys.fortune) && !compact)
            fortuneCheck(player, material, pickaxe.getPersistentDataContainer().get(EnchantKeys.fortune, PersistentDataType.INTEGER));
        if(pickaxe.getPersistentDataContainer().has(EnchantKeys.wisdom))
            wisdomCheck(player, pickaxe.getPersistentDataContainer().get(EnchantKeys.wisdom, PersistentDataType.INTEGER));
        if(pickaxe.getPersistentDataContainer().has(EnchantKeys.superbreaker))
            superBreakerCheck(player, block, pickaxe.getPersistentDataContainer().get(EnchantKeys.superbreaker, PersistentDataType.INTEGER));


        //Give all base drops to player
        ItemStack drop = new ItemStack(material, 1);
        if(compact){
            player.sendMessage("COMPACT");
            //MAKE CUSTOM ITEMS THAT WILL BE DROPPED INSTEAD OF THE BLOCK TIER
            ItemMeta tempMeta = drop.getItemMeta();
            tempMeta.addEnchant(Enchantment.LURE, 1, true);
            tempMeta.displayName(ComponentUtils.makeText("Enchanted Cobblestone", NamedTextColor.YELLOW, false));
            drop.setItemMeta(tempMeta);
        }

        player.getInventory().addItem(drop);
        playerManager.addBlocksMined(player, 1);
        player.giveExp(blockDataFunctions.getBlockExperience(player));
        //Sound and Action Bar
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.3f, 0.3f);
        player.sendActionBar(Component.text("Blocks Mined: " + playerManager.getBlocksMined(player)).append(Component.text(" Experience Gain: " + 1)));

        //Assume item is pickaxe because of check
        ItemMeta meta = pickaxe.getItemMeta();
        meta = updatePickaxeLore(meta, blockDataFunctions.getBlockMaterial(player));
        pickaxe.setItemMeta(meta);
    }

    public boolean isSuperbreakerActive(Player player){
        return superbreakerUsers.containsKey(player.getUniqueId());
    }

    public void superBreakerCheck(Player player, Block block, int superbreakerLevel){
        //Super Breaker Chance
        double superbreakerChance = ((Math.random() * 100) + 1);

        if(superbreakerUsers.containsKey(player.getUniqueId()) && superbreakerUsers.get(player.getUniqueId()) > 0){
            superbreakerUsers.put(player.getUniqueId(), superbreakerUsers.get(player.getUniqueId()) - 1);
            if(superbreakerUsers.get(player.getUniqueId()) == 0)
                superbreakerUsers.remove(player.getUniqueId());
        }

        if(superbreakerChance <= superbreakerLevel * 0.09f && !superbreakerUsers.containsKey(player.getUniqueId())){
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);

            Location center = block.getLocation().clone().add(0.5, 0.5, 0.5);
            Vector[] offsets = new Vector[] {
                    new Vector(0.5, 0.5, 0),    // Near top corner
                    new Vector(-0.5, 0.5, 0),   // Near top corner opposite side
                    new Vector(0, 0.5, 0.5),    // Near front top center
                    new Vector(0, 0.5, -0.5),   // Near back top center
                    new Vector(0.5, 0, 0.5),    // Bottom front corner
                    new Vector(-0.5, 0, 0.5),   // Bottom front opposite corner
                    new Vector(0.5, 0, -0.5),   // Bottom back corner
                    new Vector(-0.5, 0, -0.5),  // Bottom back opposite corner
                    new Vector(0.5, -0.5, 0),    // Near top corner
                    new Vector(-0.5, -0.5, 0),   // Near top corner opposite side
                    new Vector(0, -0.5, 0.5),    // Near front top center
                    new Vector(0, -0.5, -0.5)   // Near back top center
            };

            for (Vector offset : offsets) {
                player.spawnParticle(Particle.WAX_ON, center.clone().add(offset), 1);
            }

            /*player.spawnParticle(Particle.WAX_ON, block.getLocation(), 10);*/
            superbreakerUsers.put(player.getUniqueId(), 1 + (superbreakerLevel / 5));
        }
    }

    public void fortuneCheck(Player player, Material material, int level){
        int fortuneStat = level * 3;

        int extraBlock = fortuneStat / 100;
        int fortuneChance = (int) ((Math.random() * 100) + 1);
        if(fortuneChance <= fortuneStat%100){
            extraBlock += 1;
        }
        if(extraBlock > 0)
            player.getInventory().addItem(new ItemStack(material, extraBlock));
    }

    public void wisdomCheck(Player player, int level){
        player.giveExp((int) ( blockDataFunctions.getBlockExperience(player) * (level * 0.05f)));
    }

    public boolean compactCheck(Player player, int level){
        double compactChance = ((Math.random() * 100) + 1);
        if(compactChance <= level * 1f){
            player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1f, 1f);
            return true;
        }
        return false;
    }

    public ItemMeta updatePickaxeLore(ItemMeta meta, Material material){
        String tierRequirement = meta.getPersistentDataContainer().get(PickaxeKeys.tierRequirementKey, PersistentDataType.STRING);

        if(!tierRequirement.toLowerCase().equals(material.name().toLowerCase()))
            return meta;

        PersistentDataContainer tierBlockContainer = meta.getPersistentDataContainer();
        PersistentDataContainer tierBlockCapContainer = meta.getPersistentDataContainer();

        int cap = tierBlockCapContainer.get(PickaxeKeys.tierBlocksCapKey, PersistentDataType.INTEGER);
        int amount = tierBlockContainer.get(PickaxeKeys.tierBlocksKey, PersistentDataType.INTEGER);

        if(cap <= amount)
            return meta;

        amount += 1;
        tierBlockContainer.set(PickaxeKeys.tierBlocksKey, PersistentDataType.INTEGER, amount);
        List<Component> lore = meta.lore();
        String progress = amount + "/" + cap + " ";

        String green = "";
        String red = "";

        float loreProgress = (float)amount/(float)cap;
        float tempProgress;

        for(float i = 1; i < 31; i++){
            tempProgress = i/30;
            if(tempProgress <= loreProgress)
                green = green.concat("|");
            else
                red = red.concat("|");
        }

        lore.set(lore.size()-2, makeText(green, NamedTextColor.GREEN, false).append(makeText(red, NamedTextColor.RED, false)));
        lore.set(lore.size()-1, makeText("Mine ", NamedTextColor.GRAY, false).append(makeText(progress, NamedTextColor.GOLD, false).append(makeText(tierRequirement, NamedTextColor.GRAY, false))));
        meta.lore(lore);
        return meta;
    }

}
