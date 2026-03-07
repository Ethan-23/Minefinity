package org.evasive.me.minefinity.town.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.recipe.RecipeService;
import org.evasive.me.minefinity.customItems.itembuilder.data.BaseCustomItem;
import org.evasive.me.minefinity.town.service.TownService;
import org.evasive.me.minefinity.town.Structure;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.utils.TextConversions;

import java.util.List;

import static org.evasive.me.minefinity.utils.GenericGuiItems.fillerPane;
import static org.evasive.me.minefinity.utils.TextConversions.buildRarityColor;

public class MayorGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    private static final int PLAYER_STATS = 13;
    private static final List<Integer> townListing = List.of(28, 29, 30, 31, 32, 33, 34);
    private final TownService townService;
    private final RecipeService recipeService;

    public MayorGui(Player player) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Town Manager"));
        townService = Minefinity.getCore().getTownService();
        recipeService = new RecipeService();
        build();
    }

    @Override
    protected void build() {
        buildBackground();
        buildButtons();
        buildStats();
    }

    private void buildBackground(){
        for(int i = 0; i < INVENTORY_SIZE; i++){
            if(townListing.contains(i) || List.of(PLAYER_STATS).contains(i)) continue;
            inventory.setItem(i, fillerPane);
        }
    }

    private void buildButtons(){
        for(Structure structure : Structure.values()){
            int structureLevel = townService.getStructureLevel(player, structure);
            ItemBuilder structureItem = new ItemBuilder(structure.getBaseStructure().getDisplayMaterial(), TextConversions.parse("<gray>(<#55FFFF>"+ structureLevel +"<gray>) " + "<yellow>" + TextConversions.formatItemName(structure.name())));
            if(structure.getMaxLevel() == structureLevel){
                structureItem.addLore("<bold><green>Max Level");
            } else {
                structureItem.addLore("<yellow>Upgrade Cost:");
                structure.getUpgradeMap(structureLevel).getRecipe().forEach((key, value) -> {
                    structureItem.addLore("<gray>- " + buildRarityColor(key.getID(), ((BaseCustomItem) key.getBaseItem()).getRarity()) + " <gray>x" + value);
                });
            }
            inventory.setItem(townListing.get(structure.ordinal()), structureItem.build());
        }
    }

    private void buildStats(){
        inventory.setItem(PLAYER_STATS, new ItemBuilder(Material.PLAYER_HEAD, TextConversions.parse("<yellow>Player Stats:")).addSkullMeta(player).build());
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);

        int slot = e.getSlot();

        e.setCancelled(true);

        if (!townListing.contains(slot))return;

        ItemStack currentItem = e.getCurrentItem();

        if(currentItem == null || currentItem.isEmpty()) return;

        Structure structure = Structure.values()[townListing.indexOf(slot)];
        int currentLevel = townService.getStructureLevel(player, structure);

        boolean completedPurchase = recipeService.tryPurchaseItem(player, structure.getUpgradeMap(currentLevel));

        if(!completedPurchase){
            player.sendMessage(TextConversions.parse("<red>You do not have the correct materials!"));
            return;
        }
        townService.setStructureLevel(player, structure, currentLevel + 1);
        player.sendMessage(TextConversions.parse("<yellow>You have upgraded the towns <green>" + TextConversions.formatItemName(structure.name()) + "<yellow> to level <blue>" + currentLevel));
        rebuildInventory();
    }
}
