# Axiom Client — Fabric 1.21.1 Utility Mod

A client-side toolkit built for local/sandbox practice: packet inspection,
render injection, combat/movement helpers and a modern dark ClickGUI.

## Build

```bash
./gradlew build
```

The compiled jar is in `build/libs/axiom-client-1.0.0.jar`.

## Default keybinds

- **RSHIFT** — Open ClickGUI
- **R** — Melee Range
- **G** — Combat Assist
- **T** — Offhand Manager
- **V** — Velocity Reducer
- **B** — Scaffold
- **H** — VDS

All binds and settings are saved to `.minecraft/config/axiom-client.json`.

## Modules

- **MeleeRange** — extends entity interaction range via Mixin.
- **CombatAssist** — nearest-target auto-swing with rotation/aim options.
- **OffhandManager** — auto-moves Totem of Undying to offhand.
- **VelocityReducer** — scales knockback received on the local player.
- **Scaffold** — places blocks under your feet; optional tower mode.
- **VDS** — Player tracker, mob radar and valuable-item highlighter.
- **PacketLogger** — example inbound/outbound packet hook.

## Notice

This mod is intended for private/single-player use only. Do not use it on
servers where it violates their rules.
