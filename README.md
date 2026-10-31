# Marlow's crystal optimiser

Minecraft Java 1.21 · Fabric · Java 21.

## GitHub upload
Upload the CONTENTS of this ZIP to the repository root. The root must directly contain `build.gradle`, `settings.gradle`, `gradle.properties`, `src/`, and `.github/`. Do not nest them inside another directory.

## Build and install
Open Actions and wait for the workflow to finish. Download the artifact named `marlows-crystal-optimiser`. Use the file `marlows-crystal-optimiser-1.0.0.jar`, not a sources JAR. Install Fabric for Minecraft 1.21 and Fabric API 0.102.0+1.21, then place the JAR in `.minecraft/mods`.

Press O to open the menu. Item ID fields accept IDs such as `minecraft:diamond` and `minecraft:map`. Settings are saved in `config/marlows-crystal-optimiser.properties`.

## Scope
This is a clean, discoverable Fabric client mod foundation with a menu, configuration fields, icon and GitHub Actions build. The actual dispenser output interception, Paper winner logic, BJ map replacement, 50/50 stack count spoofing, Odd/Even selection, RR rules, dispenser GUI slot rendering and shift-click restoration are NOT implemented in this version. It does not change server inventories. Do not mistake the menu foundation for the completed dispenser illusion features.
