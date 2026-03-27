package org.evasive.me.minefinity.towns.structures.forge.pickaxeanvil.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.forge.pickaxeanvil.gui.PickaxeAnvilGui;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PickaxeAnvilCommand implements CommandExecutor {

    private final CustomItemRegistryService customItemRegistryService;

    public PickaxeAnvilCommand(CustomItemRegistryService customItemRegistryService) {
        Objects.requireNonNull(Minefinity.getCore().getCommand("pickaxeanvil")).setExecutor(this);
        this.customItemRegistryService = customItemRegistryService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player player))
            return true;
        new PickaxeAnvilGui(player, customItemRegistryService).open();
        return true;
    }
}
