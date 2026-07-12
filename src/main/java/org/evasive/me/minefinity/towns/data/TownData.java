package org.evasive.me.minefinity.towns.data;
import org.evasive.me.minefinity.playerdata.component.PlayerDataComponent;
import org.evasive.me.minefinity.towns.structures.data.Structure;

public class TownData implements PlayerDataComponent {

    private int townhallLevel;
    private int merchantLevel;
    private int forgeLevel;
    private int workshopLevel;

    public void setTownhallLevel(int townhallLevel) {
        this.townhallLevel = townhallLevel;
    }

    public void setMerchantLevel(int merchantLevel) {
        this.merchantLevel = merchantLevel;
    }

    public void setForgeLevel(int forgeLevel) {
        this.forgeLevel = forgeLevel;
    }

    public void setWorkshopLevel(int workshopLevel) {
        this.workshopLevel = workshopLevel;
    }

    public int getStructureLevel(Structure structure) {
        return switch (structure.id()) {
            case "WORLD_TOWNHALL" -> townhallLevel;
            case "WORLD_MERCHANT" -> merchantLevel;
            case "WORLD_WORKSHOP" -> workshopLevel;
            case "WORLD_FORGE" -> forgeLevel;
            default -> 0;
        };
    }
}
