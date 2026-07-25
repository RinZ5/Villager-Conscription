# Villager Conscription

A NeoForge 1.21.1 mod that changes how villages defend themselves. Instead of spawning Iron Golems out of thin air when a zombie shows up, villagers will now draft the local deadbeats.

This mod also act as a hard nerf to traditional iron farms.

## Features

- **Dynamic Conscription:** When a villager panics, they will search a 10-block radius for candidates to draft into the village guard.
- **Targeted Drafting:** Only unemployed villagers and villagers can be conscripted.
- **Vanilla Cooldown Integration:** Conscription utilizes the vanilla `GOLEM_DETECTED_RECENTLY` memory module, applying a 30-second cooldown to nearby villagers to prevent accidentally draft every jobless person into local militia.
- **Anti-Iron Farm:** By replacing the panic-spawn mechanic, this mod effectively disables traditional panic-based Iron Farms.

## Installation

The mod is being upload to CurseForge and Modrinth.

## For Developers

This mod utilizes Mixins to intercept the `spawnGolemIfNeeded` method within the vanilla `Villager` class.

To build from source:

```bash
git clone https://github.com/RinZ5/Villager-Conscription
cd villager-conscription
./gradlew build
```
