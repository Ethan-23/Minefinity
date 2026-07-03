package org.evasive.me.minefinity.towns.structures.townhall.mayor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.gui.GuiUtils;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.data.BaseItemRecipe;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.data.RecipeRequirement;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.service.RecipeService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.milestones.MilestoneService;
import org.evasive.me.minefinity.towns.structures.data.Structure;
import org.evasive.me.minefinity.towns.structures.service.StructureService;

import java.text.DecimalFormat;
import java.util.List;

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
            CustomItemBuilder structureItem = new CustomItemBuilder(structure.displayMaterial(), TextConversions.parse("<gray>(<#55FFFF>"+ structureLevel +"<gray>) " + "<yellow>" + TextConversions.formatItemName(structure.name())));
            if(structure.getMaxLevel() == structureLevel){
                structureItem.addLore("<bold><green>Max Level");
            } else {
                structureItem.addLore("<yellow>Milestones Required:");
                List<RecipeRequirement> recipeRequirementList = structure.getUpgradeMap(structureLevel).getRequirements();
                for(RecipeRequirement recipeRequirement : recipeRequirementList){

                    if(recipeService.checkRecipeRequirement(player, recipeRequirement))
                        structureItem.addLore("<green>- <strikethrough>" + recipeRequirement.getDisplay());
                    else
                        structureItem.addLore("<red>- " + recipeRequirement.getDisplay());
                }

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
        inventory.setItem(PLAYER_STATS, new CustomItemBuilder(Material.PLAYER_HEAD, TextConversions.parse("<yellow>Player Stats:")).addSkullMeta(player).build());
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

        BaseItemRecipe baseItemRecipe = structure.getUpgradeMap(currentLevel);

        if(baseItemRecipe == null) {
            return;
        }

        List<RecipeRequirement> recipeRequirementList = baseItemRecipe.getRequirements();
        for(RecipeRequirement recipeRequirement : recipeRequirementList){

            if(!recipeService.checkRecipeRequirement(player, recipeRequirement)){
                player.sendMessage(TextConversions.parse("<red>You do not meet the requirements to purchase this upgrade"));
                return;
            }
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
