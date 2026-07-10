package org.evasive.me.minefinity.mining.abilities;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.customItems.types.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.types.tools.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.PartAbilityComponent;
import org.evasive.me.minefinity.mining.context.BreakContext;
import org.evasive.me.minefinity.mining.context.HitContext;
import org.evasive.me.minefinity.mining.context.StatsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class MiningAbilityRunnerTest {

    private MiningAbilityRegistry registry;
    private MiningAbilityRunner runner;

    @BeforeEach
    void setUp() {
        registry = mock(MiningAbilityRegistry.class);
        runner = new MiningAbilityRunner(registry);
    }

    private BasePartItem partWithAbilities(String... abilityIds) {
        PartAbilityComponent component = new PartAbilityComponent();
        component.setValue(new ArrayList<>(List.of(abilityIds)));
        BasePartItem part = mock(BasePartItem.class);
        when(part.abilityComponent()).thenReturn(component);
        return part;
    }

    // The runner resolves a pickaxe's installed parts through BaseToolItem.getInstalledParts(),
    // so the test stubs that directly rather than going through the registry service.
    private BasePickaxeItem pickaxeWith(BasePartItem... parts) {
        BasePickaxeItem pickaxe = mock(BasePickaxeItem.class);
        when(pickaxe.getInstalledParts()).thenReturn(List.of(parts));
        return pickaxe;
    }

    private HitContext hitContext() {
        return new HitContext(mock(Player.class), null, new StatsContext());
    }

    @Test
    void onHitFiresTheAbilityBelongingToAnInstalledPart() {
        MiningAbility ability = mock(MiningAbility.class);
        when(registry.getAbility("EARLY_BIRD")).thenReturn(ability);
        BasePartItem head = partWithAbilities("EARLY_BIRD");

        HitContext ctx = hitContext();
        BasePickaxeItem pickaxe = pickaxeWith(head);

        runner.runOnHit(pickaxe, ctx);

        verify(ability).onHit(ctx);
    }

    @Test
    void theSameAbilityAcrossTwoPartsOnlyFiresOnce() {
        MiningAbility ability = mock(MiningAbility.class);
        when(registry.getAbility("EARLY_BIRD")).thenReturn(ability);
        BasePartItem head = partWithAbilities("EARLY_BIRD");
        BasePartItem core = partWithAbilities("EARLY_BIRD");

        HitContext ctx = hitContext();
        BasePickaxeItem pickaxe = pickaxeWith(head, core);

        runner.runOnHit(pickaxe, ctx);

        verify(ability, times(1)).onHit(ctx);
    }

    @Test
    void onBreakFiresTheAbilityBelongingToAnInstalledPart() {
        MiningAbility ability = mock(MiningAbility.class);
        when(registry.getAbility("METAL_DETECT")).thenReturn(ability);
        BasePartItem core = partWithAbilities("METAL_DETECT");

        BreakContext ctx = new BreakContext(mock(Player.class), null, new StatsContext());
        BasePickaxeItem pickaxe = pickaxeWith(core);

        runner.runOnBreak(pickaxe, ctx);

        verify(ability).onBreak(ctx);
    }

    @Test
    void applyStatsFiresTheAbilityBelongingToAnInstalledPart() {
        MiningAbility ability = mock(MiningAbility.class);
        when(registry.getAbility("METAL_DETECT")).thenReturn(ability);
        BasePartItem core = partWithAbilities("METAL_DETECT");

        HitContext ctx = hitContext();
        BasePickaxeItem pickaxe = pickaxeWith(core);

        runner.runApplyStats(pickaxe, ctx);

        verify(ability).applyStats(ctx);
    }

    @Test
    void aPickaxeWithNoPartsInvokesNoAbilities() {
        BasePickaxeItem pickaxe = pickaxeWith();

        runner.runOnHit(pickaxe, hitContext());

        verify(registry, never()).getAbility(any());
    }

    @Test
    void anAbilityIdWithNoRegisteredImplementationIsSkipped() {
        // registry.getAbility returns null (unstubbed) -> the runner must skip without throwing.
        BasePartItem head = partWithAbilities("EARLY_BIRD");

        BasePickaxeItem pickaxe = pickaxeWith(head);

        runner.runOnHit(pickaxe, hitContext());   // must not throw

        verify(registry).getAbility("EARLY_BIRD");
    }
}
