package org.evasive.me.minefinity.mining.abilities;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.customItems.itembuilder.data.PartSlots;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.PartAbilityComponent;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.context.BreakContext;
import org.evasive.me.minefinity.mining.context.HitContext;
import org.evasive.me.minefinity.mining.context.StatsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class MiningAbilityRunnerTest {

    private MiningAbilityRegistry registry;
    private CustomItemRegistryService service;
    private MiningAbilityRunner runner;

    @BeforeEach
    void setUp() throws Exception {
        registry = mock(MiningAbilityRegistry.class);
        runner = new MiningAbilityRunner(registry);

        // The runner resolves a pickaxe's parts through the CustomItemRegistryService singleton
        // (via ToolItemData). Static mocking isn't available in this environment, so we install a
        // mock as the singleton instance and stub part resolution on it directly.
        service = mock(CustomItemRegistryService.class);
        Field instance = CustomItemRegistryService.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, service);
    }

    private BasePartItem partWithAbilities(String... abilityIds) {
        PartAbilityComponent component = new PartAbilityComponent();
        component.setValue(new ArrayList<>(List.of(abilityIds)));
        BasePartItem part = mock(BasePartItem.class);
        when(part.abilityComponent()).thenReturn(component);
        return part;
    }

    private BasePickaxeItem pickaxeWith(Map<PartSlots, String> parts) {
        BasePickaxeItem pickaxe = mock(BasePickaxeItem.class);
        when(pickaxe.getPartMap()).thenReturn(parts);
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
        when(service.getBaseItemById("head")).thenReturn(head);

        HitContext ctx = hitContext();
        BasePickaxeItem pickaxe = pickaxeWith(Map.of(PartSlots.PICKAXE_HEAD, "head"));

        runner.runOnHit(pickaxe, ctx);

        verify(ability).onHit(ctx);
    }

    @Test
    void theSameAbilityAcrossTwoPartsOnlyFiresOnce() {
        MiningAbility ability = mock(MiningAbility.class);
        when(registry.getAbility("EARLY_BIRD")).thenReturn(ability);
        BasePartItem head = partWithAbilities("EARLY_BIRD");
        BasePartItem core = partWithAbilities("EARLY_BIRD");
        when(service.getBaseItemById("head")).thenReturn(head);
        when(service.getBaseItemById("core")).thenReturn(core);

        HitContext ctx = hitContext();
        BasePickaxeItem pickaxe = pickaxeWith(Map.of(
                PartSlots.PICKAXE_HEAD, "head",
                PartSlots.PICKAXE_CORE, "core"));

        runner.runOnHit(pickaxe, ctx);

        verify(ability, times(1)).onHit(ctx);
    }

    @Test
    void onBreakFiresTheAbilityBelongingToAnInstalledPart() {
        MiningAbility ability = mock(MiningAbility.class);
        when(registry.getAbility("METAL_DETECT")).thenReturn(ability);
        BasePartItem core = partWithAbilities("METAL_DETECT");
        when(service.getBaseItemById("core")).thenReturn(core);

        BreakContext ctx = new BreakContext(mock(Player.class), null, new StatsContext());
        BasePickaxeItem pickaxe = pickaxeWith(Map.of(PartSlots.PICKAXE_CORE, "core"));

        runner.runOnBreak(pickaxe, ctx);

        verify(ability).onBreak(ctx);
    }

    @Test
    void aPickaxeWithNoPartsInvokesNoAbilities() {
        BasePickaxeItem pickaxe = pickaxeWith(Map.of());

        runner.runOnHit(pickaxe, hitContext());

        verify(registry, never()).getAbility(any());
    }

    @Test
    void anAbilityIdWithNoRegisteredImplementationIsSkipped() {
        // registry.getAbility returns null (unstubbed) -> the runner must skip without throwing.
        BasePartItem head = partWithAbilities("EARLY_BIRD");
        when(service.getBaseItemById("head")).thenReturn(head);

        BasePickaxeItem pickaxe = pickaxeWith(Map.of(PartSlots.PICKAXE_HEAD, "head"));

        runner.runOnHit(pickaxe, hitContext());   // must not throw

        verify(registry).getAbility("EARLY_BIRD");
    }
}
