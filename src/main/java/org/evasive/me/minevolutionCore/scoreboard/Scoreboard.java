package org.evasive.me.minevolutionCore.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.resourceblock.BaseBlock;
import org.evasive.me.minevolutionCore.resourceblock.BlockType;
import org.evasive.me.minevolutionCore.utils.EconUtils;
import org.evasive.me.minevolutionCore.utils.Messages;

import java.time.LocalDateTime;

import static org.evasive.me.minevolutionCore.customItems.ItemNameBuilder.formatItemName;
import static org.evasive.me.minevolutionCore.utils.EnchantUtils.intToRoman;

public class Scoreboard {

    public void setupMainScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard mainBoard = manager.getNewScoreboard(); // Creating a new scoreboard for main stats

        Objective mainObjective = mainBoard.registerNewObjective("mainStats", Criteria.create("mainStats"),
                Messages.parse(" <bold><gradient:#55FFFF:#00AAAA>Minefinity</gradient></bold> <gray>" + LocalDateTime.now().getMonthValue() + "/" + LocalDateTime.now().getDayOfMonth()));

        mainObjective.setDisplaySlot(DisplaySlot.SIDEBAR); // Set the display slot for the main scoreboard

        updateMainScores(player, mainBoard, mainObjective); // Update the main stats
        player.setScoreboard(mainBoard); // Assign the main scoreboard
    }

    public void updateMainScores(Player player, org.bukkit.scoreboard.Scoreboard board, Objective objective) {
        // Clear existing main scores
        for (String entry : board.getEntries()) {
            board.resetScores(entry);
        }

        int playerBlockTier = MinevolutionCore.playerManager.getBlockTier(player);
        String blockTierRoman = intToRoman(playerBlockTier + 1);
        BaseBlock block = BlockType.values()[playerBlockTier].getBlock();

        // Here you can add other main stats (e.g., player currency, login time, etc.)
        addScore(board, objective, "§r", 9);
        addScore(board, objective, "§e§lAccount", 8);
        addScore(board, objective, String.format("  %s",player.getName()), 7);
        addScore(board, objective, "§r ", 6);
        addScore(board, objective, "§6§lBlock Tier", 5);
        addScore(board, objective, "  §l" + blockTierRoman + " §r" + block.name(), 4);
        addScore(board, objective, "§r  ", 3);
        addScore(board, objective, "§a§lBalance", 2);
        addScore(board, objective, "  $"+ EconUtils.getMoney(player.getUniqueId()), 1);
        // Other main stats can go here
    }

    private void addScore(org.bukkit.scoreboard.Scoreboard board, Objective obj, String text, int score) {
        obj.getScore(text).setScore(score);
    }

    public void repeatingScoreboardUpdate() {
        Bukkit.getScheduler().runTaskTimer(MinevolutionCore.core, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                setupMainScoreboard(player);
            }
        }, 0L, 100L);
    }

}
