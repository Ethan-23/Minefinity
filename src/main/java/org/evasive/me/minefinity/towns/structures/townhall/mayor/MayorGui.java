package org.evasive.me.minefinity.towns.structures.townhall.mayor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.gui.GuiUtils;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.data.BaseItemRecipe;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.service.RecipeService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.milestones.MilestoneService;
import org.evasive.me.minefinity.towns.structures.data.Structure;
import org.evasive.me.minefinity.towns.structures.service.StructureService;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import static org.evasive.me.minefinity.core.utils.TextConversions.buildRarityColor;

public class MayorGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    private static final int PLAYER_STATS = 13;
    private static final List<Integer> townListing = List.of(28, 29, 30, 31, 32, 33, 34);
    private final StructureService structureService;
    private final MilestoneService milestoneService;
    private final RecipeService recipeService;
    private final CustomItemRegistryService customItemRegistryService;

    public MayorGui(Player player, CustomItemRegistryService customItemRegistryService, StructureService structureService, RecipeService recipeService, MilestoneService milestoneService) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Town Manager"));
        this.milestoneService = milestoneService;
        this.customItemRegistryService = customItemRegistryService;
        this.structureService = structureService;
        this.recipeService = recipeService;
        build();
    }

    @Override
    protected void build() {
        GuiUtils.fillGui(inventory);
        buildButtons();
        buildStats();
    }

    private void buildButtons(){
        int index = 0;
        for(Structure structure : structureService.getStructures()){
            int structureLevel = structureService.getStructureLevel(player, structure);
            ItemBuilder structureItem = new ItemBuilder(structure.displayMaterial(), TextConversions.parse("<gray>(<#55FFFF>"+ structureLevel +"<gray>) " + "<yellow>" + TextConversions.formatItemName(structure.name())));
            if(structure.getMaxLevel() == structureLevel){
                structureItem.addLore("<bold><green>Max Level");
            } else {
                structureItem.addLore("<yellow>Milestones Required:");
                structure.getMilestoneRequirements(structureLevel).forEach((key, value) -> {
                    int milestoneLevel = milestoneService.getTier(player, key);
                    if(milestoneLevel < value)
                        structureItem.addLore("<red>- " + TextConversions.formatItemName(key) + " " + TextConversions.intToRoman(value));
                    else
                        structureItem.addLore("<green>- <strikethrough>" + TextConversions.formatItemName(key) + " " + TextConversions.intToRoman(value));
                });
                structureItem.addLore("<yellow>Upgrade Cost:");
                BaseItemRecipe structureRecipe = structure.getUpgradeMap(structureLevel);
                float cost = structureRecipe.getCost();
                if(cost > 0){
                    DecimalFormat df = new DecimalFormat("#,###.###");
                    structureItem.addLore("<gray>- <green>$" + df.format(cost));
                }
                structureRecipe.getRecipe().forEach((key, value) -> {
                    structureItem.addLore("<gray>- " + buildRarityColor(key, (customItemRegistryService.getRegisteredBaseItem(key)).getRarity()) + " <gray>x" + value);
                });


            }
            inventory.setItem(townListing.get(index), structureItem.build());
            index++;
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

        //List is random every time atm I think
        Structure structure = structureService.getStructures().stream().toList().get(townListing.indexOf(slot));
        int currentLevel = structureService.getStructureLevel(player, structure);

        for(Map.Entry<String, Integer> entry : structure.getMilestoneRequirements(currentLevel).entrySet()){
            int milestoneLevel = milestoneService.getTier(player, entry.getKey());
            if(milestoneLevel < entry.getValue()){
                player.sendMessage(TextConversions.parse("<red>You have not reached the correct milestone levels."));
                return;
            }
        }

        BaseItemRecipe baseItemRecipe = structure.getUpgradeMap(currentLevel);

        if(baseItemRecipe == null) {
            return;
        }

        boolean completedPurchase = recipeService.tryPurchaseItem(player, baseItemRecipe);

        if(!completedPurchase){
            player.sendMessage(TextConversions.parse("<red>You do not have the correct materials!"));
            return;
        }
        structureService.setStructureLevel(player, structure, currentLevel + 1);
        player.sendMessage(TextConversions.parse("<yellow>You have upgraded the towns <green>" + TextConversions.formatItemName(structure.name()) + "<yellow> to level <blue>" + (currentLevel + 1)));
        rebuildInventory();
    }
}
