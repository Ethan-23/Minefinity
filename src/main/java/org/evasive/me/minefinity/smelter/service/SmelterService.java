package org.evasive.me.minefinity.smelter.service;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.itembuilder.data.BaseFuelItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItem;
import org.evasive.me.minefinity.customItems.framework.CustomItemStack;
import org.evasive.me.minefinity.customItems.itembuilder.registry.CustomItemRegistry;
import org.evasive.me.minefinity.database.service.DirtyPlayerService;
import org.evasive.me.minefinity.player.PlayerManager;
import org.evasive.me.minefinity.smelter.Smelter;
import org.evasive.me.minefinity.smelter.recipes.SmelterRecipeRegistry;
import org.evasive.me.minefinity.smelter.recipes.SmelterRecipes;
import org.evasive.me.minefinity.utils.TextConversions;

import java.util.Map;

public class SmelterService {

    private final PlayerManager playerManager;
    private final DirtyPlayerService dirtyPlayerService = Minefinity.getCore().getDirtyPlayerService();

    public SmelterService(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    private Smelter getSmelter(Player player){
        return playerManager.get(player).getSmelter();
    }

    public String getFuelId(Player player){
        return getSmelter(player).getFuelId();
    }

    public void setFuelTier(Player player, String fuelId){
        setLastUpdated(player);
        getSmelter(player).setFuelId(fuelId);
    }

    public int getTotalFuel(Player player){
        return getSmelter(player).getTotalFuel();
    }

    public void setTotalFuel(Player player, int amount){
        setLastUpdated(player);
        getSmelter(player).setTotalFuel(amount);
    }

    public int getFuelEfficiency(Player player){
        return getSmelter(player).getRemainingFuelEfficiency();
    }

    public void setFuelEfficiency(Player player, int amount){
        getSmelter(player).setRemainingFuelEfficiency(amount);
    }

    public int getCurrentSmeltProgress(Player player){
        return getSmelter(player).getCurrentSmeltProgress();
    }

    public void setCurrentSmeltProgress(Player player, int amount){
        getSmelter(player).setCurrentSmeltProgress(amount);
    }

    public void setFuel(Player player, String fuelId, int amount){
        setLastUpdated(player);
        setFuelTier(player, fuelId);
        if(getFuelEfficiency(player) <= 0){
            setFuelEfficiency(player, ((BaseFuelItem)CustomItemRegistry.getByID(fuelId).getBaseItem()).getFuelAmount());
            amount--;
        }
        setTotalFuel(player, amount);
    }

    public void addFuel(Player player, int amount){
        setLastUpdated(player);
        setTotalFuel(player, getTotalFuel(player) + amount);
    }

    public CustomItemStack[] getInventory(Player player){
        return getSmelter(player).getInventoryItems();
    }

    public void setInventoryItem(Player player, CustomItemStack item, int slot){
        setLastUpdated(player);
        getSmelter(player).getInventoryItems()[slot] = item;
    }

    public CustomItemStack getInventoryItem(Player player, int slot){
        return getSmelter(player).getInventoryItems()[slot];
    }

    public void setInventory(Player player, CustomItemStack[] items){
        setLastUpdated(player);
        getSmelter(player).setInventoryItems(items);
    }

    public String getCurrentlySmelting(Player player){
        return getSmelter(player).getCurrentlySmelting();
    }

    public Map<String, Integer> getOutput(Player player){
        return getSmelter(player).getOutput();
    }

    public void removeSmelterStorage(Player player, String itemId, int amount){
        getSmelter(player).removeItemStorage(itemId, amount);
    }

    public boolean isEmpty(Player player){
        return getSmelter(player).isEmpty();
    }

    public void setCurrentlySmelting(Player player, String itemId){
        setLastUpdated(player);
        getSmelter(player).setCurrentlySmelting(itemId);
    }

    //Smelting Process
    public void attemptSmelt(Player player, int progressTime){

        String currentId = getCurrentlySmelting(player);

        if(currentId == null && isEmpty(player))
            return;

        int remainingFuel = getFuelEfficiency(player);

        if(getTotalFuel(player) <= 0 && remainingFuel <= 0)
            return;

        remainingFuel = attemptRefuel(player);

        if(attemptSmeltProgress(player, currentId, remainingFuel, progressTime))
            return;

        findNewRecipe(player);
    }

    public boolean attemptSmeltProgress(Player player, String currentId, int remainingFuel, int progressTime){
        if(currentId == null)
            return false;

        int currentProgress = getCurrentSmeltProgress(player);

        //int totalFuelCost = SmelterRecipes.valueOf(currentId).getSmelterRecipe().getFuelCost();
        //int remainingCost = totalFuelCost - currentProgress;
        int fuelToBurn = Math.min(progressTime, remainingFuel);
        //fuelToBurn = Math.min(fuelToBurn, remainingCost);
        int progress = currentProgress + fuelToBurn;

        setCurrentSmeltProgress(player, progress);

        remainingFuel -= fuelToBurn;
        setFuelEfficiency(player, remainingFuel);

        attemptRefuel(player);

        setLastUpdated(player);

        //if(progress < SmelterRecipes.valueOf(currentId).getSmelterRecipe().getFuelCost()) return true;

        setCurrentSmeltProgress(player, 0);
        getOutput(player).put(currentId, getOutput(player).getOrDefault(currentId, 0) + 1);
        setCurrentlySmelting(player, null);

        findNewRecipe(player);
        return true;
    }

    private int attemptRefuel(Player player){

        int remainingFuel = getFuelEfficiency(player);

        if(getTotalFuel(player) <= 0)
            return remainingFuel;

        if(remainingFuel <= 0){
            setTotalFuel(player, getTotalFuel(player) - 1);
            setFuelEfficiency(player, ((BaseFuelItem)CustomItemRegistry.getByID(getFuelId(player))).getFuelAmount());
            remainingFuel = getFuelEfficiency(player);
        }
        return remainingFuel;
    }

    private void findNewRecipe(Player player){
        for(CustomItemStack customItemStack : getInventory(player)){
            if(customItemStack == null) continue;

            CustomItem customItem = customItemStack.getCustomItem();
            int amount = customItemStack.getAmount();

            SmelterRecipes recipe = SmelterRecipeRegistry.getRecipe(customItem);

            if(recipe == null) continue;

            //Map<CustomItem, Integer> smelterRecipe = recipe.getSmelterRecipe().getRecipe();

            //if(amount < smelterRecipe.get(customItem)) continue;

            //customItemStack.setAmount(customItemStack.getAmount() - smelterRecipe.get(customItemStack.getCustomItem()));
            //setCurrentlySmelting(player, recipe.getSmelterRecipe().getResult().getID());
            return;
        }

    }

    public void addOfflineProgress(Player player){

        boolean madeProgress = false;

        int progressTime = Math.toIntExact(System.currentTimeMillis() / 1000 - getLastUpdated(player) / 1000);

        String fuelId = getFuelId(player);
        if(getTotalFuel(player) == 0 && getFuelEfficiency(player) == 0 || fuelId == null) return;

        int totalFuelValue = getFuelEfficiency(player) + getTotalFuel(player) * ((BaseFuelItem)CustomItemRegistry.getByID(fuelId).getBaseItem()).getFuelAmount();

        for(int i = 0; i < getInventory(player).length; i++){

            if(totalFuelValue == 0 || progressTime == 0) break;

            CustomItemStack customItemStack = getInventory(player)[i];
            if(customItemStack == null) continue;
            CustomItem customItem = customItemStack.getCustomItem();
            SmelterRecipes smelterRecipes = SmelterRecipeRegistry.getRecipe(customItem);

            if(smelterRecipes == null) continue;

            //int fuelCost = smelterRecipes.getSmelterRecipe().getFuelCost();
            //int amountPerCraft = smelterRecipes.getSmelterRecipe().getRecipe().get(customItemStack.getCustomItem());
            //int amount = customItemStack.getAmount() / amountPerCraft;

            //if(amount == 0) continue;

            //String outputId = smelterRecipes.getSmelterRecipe().getResult().getID();

            /*if(fuelCost * amount > totalFuelValue || fuelCost * amount > progressTime){
                int smeltableCount = Math.min(totalFuelValue / fuelCost, progressTime / fuelCost);
                if(smeltableCount == 0) continue;
                bulkSmelt(player, smeltableCount, customItemStack, i, outputId);
                totalFuelValue -= smeltableCount * fuelCost;
                progressTime -= smeltableCount * fuelCost;
                customItemStack.setAmount(customItemStack.getAmount() - (smeltableCount * amountPerCraft));
                madeProgress = true;
                continue;
            }

            totalFuelValue -= fuelCost * amount;
            progressTime -= fuelCost * amount;
            bulkSmelt(player, amount, customItemStack, i, outputId);
            customItemStack.setAmount(customItemStack.getAmount() % amountPerCraft);
            setInventoryItem(player, customItemStack, i);
            madeProgress = true;
            */
        }

        if(!madeProgress) return;

        BaseFuelItem baseFuelItem = (BaseFuelItem) CustomItemRegistry.getByID(fuelId);

        setTotalFuel(player, totalFuelValue / baseFuelItem.getFuelAmount());
        setFuelEfficiency(player, totalFuelValue % baseFuelItem.getFuelAmount());
        player.sendMessage(TextConversions.parse("<gold>Smelter has made progress while you were offline!"));
    }

    private void bulkSmelt(Player player, int smeltCount, CustomItemStack customItemStack, int slot, String outputId){
        setInventoryItem(player, customItemStack, slot);
        addOutput(player, outputId, smeltCount);
    }

    public void addOutput(Player player, String itemId, int amount){
        setLastUpdated(player);
        int current = getOutput(player).getOrDefault(itemId, 0);
        getOutput(player).put(itemId, current + amount);
    }

    public long getLastUpdated(Player player){
        return getSmelter(player).getLastUpdated();
    }

    public void setLastUpdated(Player player){
        dirtyPlayerService.addDirtyPlayer(player);
        getSmelter(player).setLastUpdated(System.currentTimeMillis());
    }
}
