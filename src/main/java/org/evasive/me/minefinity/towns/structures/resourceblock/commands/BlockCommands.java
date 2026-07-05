package org.evasive.me.minefinity.towns.structures.resourceblock.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.economy.EconomyService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.milestones.MiningMilestoneService;
import org.evasive.me.minefinity.towns.structures.resourceblock.gui.BlockGui;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class BlockCommands implements CommandExecutor {

    private final BlockTierService blockTierService;
    private final MiningMilestoneService milestoneService;
    private final EconomyService economyService;
    private final CustomItemRegistryService customItemRegistryService;

    public BlockCommands(BlockTierService blockTierService, MiningMilestoneService milestoneService, EconomyService economyService, CustomItemRegistryService customItemRegistryService){
        Objects.requireNonNull(Minefinity.getCore().getCommand("blocks")).setExecutor(this);
        this.blockTierService = blockTierService;
        this.milestoneService = milestoneService;
        this.economyService = economyService;
        this.customItemRegistryService = customItemRegistryService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NonNull [] strings) {
        if (!(commandSender instanceof Player player))
            return true;
        new BlockGui(player, blockTierService, customItemRegistryService, milestoneService, economyService, player.getWorld().getName()).open();
        return true;
    }
}
