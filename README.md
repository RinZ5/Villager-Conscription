# Villager Conscription

Tired of Iron Golems magically appearing out of thin air whenever a zombie gets too close? **Villager Conscription** is a NeoForge 1.21.1 add-on for the [Guard Villagers](https://www.curseforge.com/minecraft/mc-mods/guard-villagers) mod that completely changes how villages defend themselves!

Instead of summoning a golem, panicking villagers will now draft the local deadbeats to protect their home. If a zombie attacks, the nearest unemployed villager (or nitwit) is instantly handed a sword and told to get to work! 

*(Note: Because this replaces how iron golems spawn when villagers panic, this mod effectively disables traditional iron golem farms!)*

## What Does It Do?

- **Drafting the Unemployed:** When a regular villager panics from seeing a monster, they will look around for any jobless villagers or nitwits and instantly conscript them into a Guard Villager.
- **No Infinite Armies:** There is a built-in cooldown. Villagers won't accidentally conscript the entire town at once just because a single zombie showed up.
- **Goodbye Iron Farms:** By replacing the vanilla panic-spawning mechanic with conscription, this mod acts as a hard counter to standard iron farms.
- **Data-Driven Customization:** Fully supports datapacks! Modpack developers and players can completely customize how different villager professions behave through JSON files. Configure custom gear loadouts, equipment drop chances, and unique conversion sound and particle effects per profession.

## Installation

The mod is available on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/villager-conscription), or you can download the latest version from [release page](https://github.com/RinZ5/Villager-Conscription/releases) directly.

## For Developers

This mod utilizes Mixins to intercept the `spawnGolemIfNeeded` method within the vanilla `Villager` class.

To build from source:

```bash
git clone https://github.com/RinZ5/Villager-Conscription
cd villager-conscription
./gradlew build
