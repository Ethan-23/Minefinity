package org.evasive.me.minefinity.towns.structures.workshop.engineer.tools;

public enum WorkshopToolsTiers {
    //Make into a config
    FLINT(5),
    BRONZE_INGOT(10),
    STEEL_INGOT(20),
    ENERGIZED_CRYSTAL(40),
    GEMSTONE(80);

    final int durability;

    WorkshopToolsTiers(int durability) {
        this.durability = durability;
    }

    public int getDurability() {
        return durability;
    }

    public static boolean contains(String value) {
        for (WorkshopToolsTiers toolsTiers : WorkshopToolsTiers.values()) {
            if (toolsTiers.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
