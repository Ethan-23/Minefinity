# Minefinity

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.11-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![Platform](https://img.shields.io/badge/Platform-Paper-blue)

*A packet-driven Minecraft progression plugin where every player shares one world but sees their own version of it. Mine an endless resource block at the center, pour what you gather into your town, and unlock upgrades, automation, and entire new areas — all rendered per-player so your progress is yours alone.*

> Inspired by the classic Minecraft map [minevolution](https://www.curseforge.com/minecraft/worlds/minevolution).

---

## Gameplay

At the heart of the world sits an **infinite resource block**. Mine it to earn resources, then feed those resources back into your **town** to upgrade its buildings and NPCs. Each upgrade unlocks new tiers of resources, new forms of automation, and new areas — so every step forward mines faster, earns more, and pushes you further.

The twist: the world is **shared, but personalized**. Using packets, the server shows each player their *own* upgraded structures and unlocked areas layered over the same physical world. Two players standing in the same spot can see completely different towns based on how far each has progressed.

**The core loop:**

```
Mine the resource block  →  Gather resources  →  Upgrade town buildings & NPCs
        ↑                                                      │
        └──────  Unlock new tiers, automation & areas  ◄───────┘
```

### The Town

Your town is a hub of buildings and NPCs, each offering a different way to craft, trade, sell, automate, or gather. Visit the **Forge** to craft, the **Goblin Tinkerer** to tinker and trade, the **Miner** to automate resource gathering, and more — each one a station you invest in to advance.

### Custom Mining & Pickaxe Upgrades

Mining is **fully custom-coded** from the ground up (not vanilla block breaking). Your pickaxe isn't a fixed tool — you **craft parts in the Forge and apply them** to build and upgrade your own pickaxe, mixing parts to gain new abilities and mine higher-tier blocks.

---

## Architecture

Minefinity is built as five focused modules, wired together at startup with explicit dependency injection. Each module owns a clear slice of the plugin, and dependencies flow one direction — from the foundation upward.

| Module | Responsibility |
|--------|----------------|
| **Player Data** | Persistence, ranks, permissions, stats — the foundation everything builds on |
| **Core** | Shared framework: economy, GUIs, NPCs, admin tools, worlds |
| **Custom Items** | The item engine — component-based custom items, backpacks, recipes |
| **Mining** | Fully custom packet-based mining, block tiers, and pickaxe abilities |
| **Towns** | The progression layer — structures, automation, and per-player world rendering |

---

## Modules

### 🗄️ Player Data

The foundation module. Owns everything about *who a player is* and makes sure it survives restarts. It has zero feature dependencies — everything else depends on it, not the other way around.

- **Persistence** — player data is stored in **MySQL** behind a **HikariCP** connection pool, loaded asynchronously during pre-login (so no lag on join and data is guaranteed ready before a player enters the world).
- **Component-based data model** — instead of one giant player object, each feature registers its own data "slice" (forge items, auto-miner state, milestones, town data, engineer data). Features own their data without the foundation knowing about them.
- **Ranks & Permissions** — separate **staff** and **donor** rank tracks, each with configurable prefixes and permission sets applied on join.
- **Stats** — a contributor-based stat system where items and features can add to a player's stats.
- **Presentation** — scoreboards and **Lunar Client nametags** (via Apollo) for rank prefixes and player info.

### ⚙️ Core

The shared toolbox every other module draws on. If it's a framework-level system rather than a specific feature, it lives here.

- **Economy** — balances, `/pay`, and admin economy commands (`add` / `set` / `subtract`).
- **GUI framework** — a reusable base for menus, including a generic confirmation dialog and shared GUI items.
- **Custom NPC framework** — packet-based NPCs with pluggable behaviors (e.g. combat NPCs), spawning, and interaction handling — no real entities, so they're cheap and fully controlled.
- **Admin & staff tools** — vanish, staff mode, invsee, gamemode shortcuts, speed, rename, spawn/warp management.
- **Worlds** — custom world generation including a void generator for bespoke areas.
- **Supporting systems** — a rarity system, block-type registries, spawn/warp services, and shared utilities (text, time, economy formatting, tab completion).

### 🎒 Custom Items

A full item-building engine. Items aren't hardcoded — they're **composed from reusable components**, so new items are assembled from behavior pieces rather than written from scratch.

- **Item builder & components** — mix and match capabilities onto any item: `Glow`, `Soulbound`, `StackSize`, `FlavorText`, `Stats`, `EquipmentSlot`, `VisualMaterial`, `Value`, `Fuel`, and storage components.
- **Tool parts** — dedicated part components (`ToolPart`, `PartAbility`, `PartSlot`) that power the craftable, upgradeable pickaxe system.
- **Backpacks** — expandable item storage with its own GUI, pickup handling, and persistence.
- **Recipes** — a custom recipe builder with requirement gating, separate from vanilla crafting.
- **Registry** — a central registry so items can be looked up, validated, and given out (`/minegive`) consistently.

### ⛏️ Mining

The custom mining engine and everything that makes breaking the resource block feel good.

- **Packet-based mining** — mining is simulated through packets rather than vanilla block breaks, giving full control over block health, tiers, and per-player state.
- **Block tiers** — a tiered block system where higher tiers yield better resources and require better tools to break.
- **Milestones** — per-block progression milestones that track and reward continued mining.
- **Pickaxe abilities** — special effects that trigger while mining, driven by the parts equipped on your pickaxe:
  - **Critical Fracture** — chance-based critical mining hits
  - **Early Bird** — time/streak-based bonuses
  - **Metal Detector** — surfaces rarer resources
  - **Structured Force** — rewards sustained mining streaks

### 🏘️ Towns

The progression layer that ties everything together — the buildings, NPCs, and the per-player world rendering that defines the game.

- **Per-player world packets** — the signature system. `MassBlockPacketSender` sends each player their own view of structures and upgrades (loaded from schematics), so the shared world reflects each player's individual progress.
- **Structures & stations**, each an upgradeable building or NPC:
  - **Forge / Blacksmith** — craft items and pickaxe parts
  - **Pickaxe Anvil** — assemble and apply parts to upgrade your pickaxe
  - **Smelter** — process raw resources into refined materials
  - **Mines / Auto-Miner** — automate resource gathering
  - **Resource Block** — the central infinite block and its tier controls
  - **Workshop / Engineer** — advanced crafting and tooling
  - **Merchant Shops** — buy and sell
  - **Town Hall / Mayor** — town-level management and progression
- **NPC integration** — structures are fronted by interactive NPCs (the Forge, the Goblin Tinkerer, the Miner, and more) using the Core NPC framework.

---

## Tech Stack

- **Language:** Java 21
- **Platform:** Paper 1.21.11
- **Build:** Gradle with the Shadow plugin (`./gradlew build` → `build/libs/Minefinity.jar`)
- **Database:** MySQL via HikariCP
- **Packets:** [PacketEvents](https://github.com/retrooper/packetevents) — powers custom mining, NPCs, and per-player world rendering
- **Dependencies:** WorldGuard (regions), WorldEdit / FAWE (schematics), Lunar Apollo (nametags), Gson (serialization)
