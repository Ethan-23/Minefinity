package org.evasive.me.minefinity.towns.structures.townhall.mayor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.gui.GuiUtils;
import org.evasive.me.minefinity.customItems.recipebuilder.service.RecipeService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.service.StructureService;
import org.evasive.me.minefinity.towns.structures.data.Structure;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.core.utils.TextConversions;

import java.util.List;

import static org.evasive.me.minefinity.core.utils.guis.GenericGuiItems.fillerPane;
import static org.evasive.me.minefinity.core.utils.TextConversions.buildRarityColor;

public class MayorGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    private static final int PLAYER_STATS = 13;
    private static final List<Integer> townListing = List.of(28, 29, 30, 31, 32, 33, 34);
    private final StructureService townService;
    private final RecipeService recipeService;
    private final CustomItemRegistryService customItemRegistryService;

    public MayorGui(Player player, CustomItemRegistryService customItemRegistryService, StructureService townService, RecipeService recipeService) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Town Manager"));
        this.customItemRegistryService = customItemRegistryService;
        this.townService = townService;
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
        for(Structure structure : Structure.values()){
            int structureLevel = townService.getStructureLevel(player, structure);
            ItemBuilder structureItem = new ItemBuilder(structure.getBaseStructure().getDisplayMaterial(), TextConversions.parse("<gray>(<#55FFFF>"+ structureLevel +"<gray>) " + "<yellow>" + TextConversions.formatItemName(structure.name())));
            if(structure.getMaxLevel() == structureLevel){
                structureItem.addLore("<bold><green>Max Level");
            } else {
                structureItem.addLore("<yellow>Upgrade Cost:");
                structure.getUpgradeMap(structureLevel).getRecipe().forEach((key, value) -> {
                    structureItem.addLore("<gray>- " + buildRarityColor(key, (customItemRegistryService.getRegisteredBaseItem(key)).getRarity()) + " <gray>x" + value);
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
