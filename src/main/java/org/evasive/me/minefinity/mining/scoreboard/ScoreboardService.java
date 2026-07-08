package org.evasive.me.minefinity.mining.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.admin.service.VanishService;
import org.evasive.me.minefinity.playerdata.economy.EconomyService;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistry;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.mining.blocks.PlayerBlockTiers;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.core.data.BaseBlock;

import java.time.LocalDateTime;

import static org.evasive.me.minefinity.core.utils.TextConversions.intToRoman;
import static org.evasive.me.minefinity.core.utils.economy.EconNumberUtils.balanceSuffix;

public class ScoreboardService {

    private final PlayerDataService playerDataService;
    private final EconomyService economyService;
    private final VanishService vanishService;
    private final BlockTypeRegistry blockTypeRegistry;

    private BukkitTask scoreboardTask;

    private static final String OBJECTIVE_ID = "mainStats";

    public ScoreboardService(PlayerDataService playerDataService, EconomyService economyService, VanishService vanishService, BlockTypeRegistry blockTypeRegistry) {
        this.playerDataService = playerDataService;
        this.economyService = economyService;
        this.vanishService = vanishService;
        this.blockTypeRegistry = blockTypeRegistry;
    }

    public void setupMainScoreboard(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = board.registerNewObjective(OBJECTIVE_ID, Criteria.DUMMY,
                TextConversions.parse(" <bold><gradient:#55FFFF:#00AAAA>Minefinity</gradient></bold> <gray>"
                        + LocalDateTime.now().getMonthValue() + "/" + LocalDateTime.now().getDayOfMonth()));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        player.setScoreboard(board);
        updateMainScores(player);
    }

    public void updateMainScores(Player player) {
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective(OBJECTIVE_ID);
        if (objective == null) {
            setupMainScoreboard(player);
            return;
        }

        for (String entry : board.getEntries())
            board.resetScores(entry);

        String worldName = player.getWorld().getName();
        int tier = playerDataService.getPlayerData(player.getUniqueId())
                .get(PlayerBlockTiers.class).getUnlockedBlockTier(worldName);
        String tierRoman = intToRoman(tier + 1);
        BaseBlock baseBlock = blockTypeRegistry.getBlock(blockTypeRegistry.getBlockList(worldName).get(tier));

        String name = vanishService.isVanished(player)
                ? "<gray>(<aqua>V<gray>) " + player.getName()
                : player.getName();

        line(objective, " ",                                          9);
        line(objective, "<yellow><bold>Account",                      8);
        line(objective, "  " + name,                                  7);
        line(objective, "  ",                                         6);
        line(objective, "<gold><bold>Block Tier",                     5);
        line(objective, "  <bold>" + tierRoman + "</bold> " + TextConversions.formatItemName(baseBlock.name()), 4);
        line(objective, "   ",                                        3);
        line(objective, "<green><bold>Balance",                       2);
        line(objective, "  $" + balanceSuffix(economyService.getBalance(player)), 1);
    }

    private void line(Objective objective, String miniMessage, int score) {
        objective.getScore(TextConversions.toLegacy(miniMessage)).setScore(score);
    }

    public void repeatingScoreboardUpdate() {
        scoreboardTask = Bukkit.getScheduler().runTaskTimer(Minefinity.getCore(),
                () -> Bukkit.getOnlinePlayers().forEach(this::updateMainScores),
                0L, 100L);
    }

    public void stop() {
        if (scoreboardTask != null)
            scoreboardTask.cancel();
    }
}
