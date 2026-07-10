package org.evasive.me.minefinity.mining.handlers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.evasive.me.minefinity.core.data.CustomItemStack;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.framework.ItemPickupService;
import org.evasive.me.minefinity.customItems.types.BaseCustomItem;
import org.evasive.me.minefinity.customItems.types.tools.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.abilities.MiningAbilityRunner;
import org.evasive.me.minefinity.mining.context.BreakContext;
import org.evasive.me.minefinity.mining.context.StatsContext;
import org.evasive.me.minefinity.mining.data.MiningDataMap;
import org.evasive.me.minefinity.mining.milestones.MiningMilestoneService;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BlockBreakHandlerTest {

    private static final String BLOCK_ID = "BlockId";
    private static final String DROP_ID = "drop";
    private static final String SPECIAL_DROP_ID = "specialDrop";

    private ItemPickupService itemPickupService;
    private MiningMilestoneService milestoneService;
    private MiningDataMap miningDataMap;
    private BlockBreakNotifier blockBreakNotifier;   // injected seam — mocks away all Bukkit/Sound I/O
    private MiningAbilityRunner runner;

    private Player player;
    private Location location;

    private BlockBreakHandler handler;

    @BeforeEach
    void setUp() {
        CustomItemRegistryService customItemRegistryService = mock(CustomItemRegistryService.class);
        itemPickupService = mock(ItemPickupService.class);
        milestoneService = mock(MiningMilestoneService.class);
        BlockTierService blockTierService = mock(BlockTierService.class);
        miningDataMap = mock(MiningDataMap.class);
        blockBreakNotifier = mock(BlockBreakNotifier.class);
        runner = mock(MiningAbilityRunner.class);

        World world = mock(World.class);
        when(world.getName()).thenReturn("world");
        player = mock(Player.class);
        when(player.getWorld()).thenReturn(world);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        location = new Location(world, 1, 2, 3);

        when(blockTierService.getSelectedMiningBlock(player, "world")).thenReturn(BLOCK_ID);
        when(itemPickupService.givePlayerDrops(any(Player.class), any(CustomItemStack.class))).thenReturn(0);

        // The handler resolves the drop id back to a base item purely to colour the notification.
        BaseCustomItem dropItem = mock(BaseCustomItem.class);
        when(dropItem.getRarity()).thenReturn(Rarity.MINOR);
        when(customItemRegistryService.getBaseItemById(anyString())).thenReturn(dropItem);

        handler = new BlockBreakHandler(customItemRegistryService, itemPickupService, milestoneService,
                blockTierService, miningDataMap, blockBreakNotifier);
    }

    private BaseBlock plainBlock() {
        return new BaseBlock("Stone", Material.STONE, 1, 100, DROP_ID, null, 0f, List.of());
    }

    private BaseBlock specialBlock() {
        return new BaseBlock("Stone", Material.STONE, 1, 100, DROP_ID, SPECIAL_DROP_ID, 0f, List.of());
    }

    private CustomItemStack captureGivenDrop() {
        ArgumentCaptor<CustomItemStack> drop = ArgumentCaptor.forClass(CustomItemStack.class);
        verify(itemPickupService).givePlayerDrops(eq(player), drop.capture());
        return drop.getValue();
    }

    @Test
    void withoutFortuneASingleBlockDropIsHandedToPickup() {
        StatsContext stats = new StatsContext();   // fortune 0

        handler.handleBlockBreak(location, player, plainBlock(), null, stats, runner);

        CustomItemStack given = captureGivenDrop();
        assertEquals(DROP_ID, given.getCustomItem());
        assertEquals(1, given.getAmount());
    }

    @Test
    void fortuneIncreasesTheAmountHandedToPickup() {
        StatsContext stats = new StatsContext();
        stats.addFortune(100f);   // guaranteed +1, remainder 0

        handler.handleBlockBreak(location, player, plainBlock(), null, stats, runner);

        assertEquals(2, captureGivenDrop().getAmount());
    }

    @Test
    void whenTheSpecialRollSucceedsTheSpecialDropIdIsGiven() {
        StatsContext stats = new StatsContext();
        stats.addSpecialChance(99);   // 1 baseline + 99 == 100 -> roll(1..100) always succeeds

        handler.handleBlockBreak(location, player, specialBlock(), null, stats, runner);

        assertTrue(stats.isSpecialDrop());
        assertEquals(SPECIAL_DROP_ID, captureGivenDrop().getCustomItem());
    }

    @Test
    void aBlockWithNoSpecialDropNeverRollsSpecialEvenAtFullChance() {
        StatsContext stats = new StatsContext();
        stats.addSpecialChance(99);   // would always succeed, but the block has no special drop id

        handler.handleBlockBreak(location, player, plainBlock(), null, stats, runner);

        assertFalse(stats.isSpecialDrop());
        assertEquals(DROP_ID, captureGivenDrop().getCustomItem());
    }

    @Test
    void breakingAdvancesPlayerMilestoneDataAndReleasesTheBlock() {
        handler.handleBlockBreak(location, player, plainBlock(), null, new StatsContext(), runner);

        verify(milestoneService).increaseBlocksMined(player, BLOCK_ID, 1);
        verify(milestoneService).increaseTierProgress(player, BLOCK_ID, 1);
        verify(miningDataMap).removeBlockPos(eq(location), any(UUID.class));
    }

    @Test
    void pickaxeAbilitiesRunOnlyWhenAPickaxeIsPresent() {
        handler.handleBlockBreak(location, player, plainBlock(), null, new StatsContext(), runner);
        verify(runner, never()).runOnBreak(any(), any());

        BasePickaxeItem pickaxe = mock(BasePickaxeItem.class);
        handler.handleBlockBreak(location, player, plainBlock(), pickaxe, new StatsContext(), runner);
        verify(runner).runOnBreak(eq(pickaxe), any(BreakContext.class));
    }

    @Test
    void theBreakNotificationCarriesTheBlockMaterialDropAmountRarityAndFlags() {
        StatsContext stats = new StatsContext();   // not special, one drop, inventory not full

        handler.handleBlockBreak(location, player, plainBlock(), null, stats, runner);

        // The BaseBlock's material (not the sponge placeholder in the world) is forwarded so the notifier
        // can resolve the real break sound.
        ArgumentCaptor<CustomItemStack> drop = ArgumentCaptor.forClass(CustomItemStack.class);
        verify(blockBreakNotifier).blockBreak(eq(player), eq(Material.STONE), drop.capture(), eq(Rarity.MINOR), eq(false), eq(false));
        assertEquals(1, drop.getValue().getAmount());
    }

    @Test
    void aFullInventoryIsReportedToTheNotifier() {
        when(itemPickupService.givePlayerDrops(any(Player.class), any(CustomItemStack.class)))
                .thenReturn(2);   // overflow left over

        handler.handleBlockBreak(location, player, plainBlock(), null, new StatsContext(), runner);

        verify(blockBreakNotifier).blockBreak(eq(player), any(Material.class), any(CustomItemStack.class), any(Rarity.class),
                eq(false), eq(true));
    }

    @Test
    void pickaxeReactionsRunAfterTheSpecialDropIsResolved() {
        StatsContext stats = spy(new StatsContext());
        BasePickaxeItem pickaxe = mock(BasePickaxeItem.class);

        handler.handleBlockBreak(location, player, specialBlock(), pickaxe, stats, runner);

        // Reactions now fire AFTER the roll is locked in, so an onBreak ability can read the resolved
        // special-drop flag. Chance contributions happen upstream in applyStats.
        InOrder inOrder = inOrder(stats, runner);
        inOrder.verify(stats).setSpecialDrop(anyBoolean());
        inOrder.verify(runner).runOnBreak(eq(pickaxe), any(BreakContext.class));
    }

    @Test
    void aReactionAbilityObservesTheResolvedSpecialDropDuringOnBreak() {
        // The pay-off of the ordering: by the time runOnBreak fires, isSpecialDrop() reflects this break,
        // so an onBreak reaction that inspects the special-drop result sees this break. (Chance is already
        // on the context via applyStats upstream.)
        StatsContext stats = new StatsContext();
        stats.addSpecialChance(99);   // 1 + 99 == 100 -> the roll is guaranteed to be special
        BasePickaxeItem pickaxe = mock(BasePickaxeItem.class);
        AtomicBoolean sawSpecial = new AtomicBoolean(false);
        doAnswer(inv -> { sawSpecial.set(stats.isSpecialDrop()); return null; })
                .when(runner).runOnBreak(eq(pickaxe), any(BreakContext.class));

        handler.handleBlockBreak(location, player, specialBlock(), pickaxe, stats, runner);

        assertTrue(sawSpecial.get(), "onBreak runs after setSpecialDrop, so a reaction sees the special result");
    }

    @Test
    void reactionsStillFireWhenTheBlockYieldsNoDrops() {
        // runOnBreak sits ABOVE the empty-drops return, so a no-drop block still procs onBreak abilities
        // by design — an ability may want to react to the swing even when there's nothing to hand out.
        // The block is still released, but nothing is given or announced.
        BaseBlock noDrop = new BaseBlock("Broken", Material.STONE, 1, 100, null, null, 0f, List.of());
        BasePickaxeItem pickaxe = mock(BasePickaxeItem.class);

        handler.handleBlockBreak(location, player, noDrop, pickaxe, new StatsContext(), runner);

        verify(runner).runOnBreak(eq(pickaxe), any(BreakContext.class));
        verify(miningDataMap).removeBlockPos(eq(location), any(UUID.class));
        verify(itemPickupService, never()).givePlayerDrops(any(Player.class), any(CustomItemStack.class));
        verify(blockBreakNotifier, never()).blockBreak(any(), any(), any(), any(), anyBoolean(), anyBoolean());
    }

    @Test
    void aBlockWithNoConfiguredDropStillCountsTheMineButGivesNothing() {
        BaseBlock broken = new BaseBlock("Broken", Material.STONE, 1, 100, null, null, 0f, List.of());

        handler.handleBlockBreak(location, player, broken, null, new StatsContext(), runner);

        // The mine still counts toward milestones and the block is released...
        verify(milestoneService).increaseTierProgress(player, BLOCK_ID, 1);
        verify(milestoneService).increaseBlocksMined(player, BLOCK_ID, 1);
        verify(miningDataMap).removeBlockPos(eq(location), any(UUID.class));
        // ...but with no valid drop, nothing is handed out or announced.
        verify(itemPickupService, never()).givePlayerDrops(any(Player.class), any(CustomItemStack.class));
        verify(blockBreakNotifier, never()).blockBreak(any(), any(), any(), any(), anyBoolean(), anyBoolean());
    }
}
