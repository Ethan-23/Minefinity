package org.evasive.me.minevolutionCore.forge.recipes;

import org.evasive.me.minevolutionCore.customItems.ItemBuilder;
import org.evasive.me.minevolutionCore.forge.recipes.components.CopperCoreRecipe;
import org.evasive.me.minevolutionCore.forge.recipes.components.StoneCoreRecipe;
import org.evasive.me.minevolutionCore.forge.recipes.components.WoodenCoreRecipe;
import org.evasive.me.minevolutionCore.forge.recipes.pickaxe.CopperPickaxeRecipe;
import org.evasive.me.minevolutionCore.forge.recipes.pickaxe.StonePickaxeRecipe;
import org.evasive.me.minevolutionCore.forge.recipes.pickaxe.WoodenPickaxeRecipe;
import org.evasive.me.minevolutionCore.forge.recipes.storage.BackpackRecipe;

public enum ForgeRecipes {
    WOODEN_PICKAXE(new WoodenPickaxeRecipe()),
    STONE_PICKAXE(new StonePickaxeRecipe()),
    COPPER_PICKAXE(new CopperPickaxeRecipe()),
    WOODEN_CORE(new WoodenCoreRecipe()),
    STONE_CORE(new StoneCoreRecipe()),
    COPPER_CORE(new CopperCoreRecipe()),
    BACKPACK(new BackpackRecipe());


    private final Object builder;

    // Constructor for BlockType enum
    ForgeRecipes(Object builder) {
        this.builder = builder;
    }

    public ItemBuilder getItemBuilder() {
        return (ItemBuilder) builder;
    }

    public ForgeItemBuilder getForgeItemBuilder(){
        return (ForgeItemBuilder) builder;
    }
}
