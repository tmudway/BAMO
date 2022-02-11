# BAMO - Block And Move On
A tool to simplify importing custom assets in Minecraft

Currently only allows you to quickly prototype models in-game, but further functionality is coming

## WARNING - STILL AN EARLY VERSION SO USE AT YOUR OWN RISK
I would **HIGHLY** recommend you backup up any model files before attempting to use this plugin/mod

## Installation
 1. Download the [BAMO.jar](https://github.com/tmudway/MinecraftMods/releases/download/0.1.0-alpha/bamo-0.1.jar) mod file and move it to the mods folder in your minecraft directory
 2. Download [Kotlin for Forge 1.16.0](https://www.curseforge.com/minecraft/mc-mods/kotlin-for-forge/files/3527736) and move it to the same directory
 3. Download the [BAMO.js](https://github.com/tmudway/MinecraftMods/releases/download/0.1.0-alpha/BAMO.js) plugin file
 4. Open Blockbench, click File -> Plugins -> Load Plugins from File
 5. Select the BAMO.js file you just downloaded
 6. Restart Blockbench
 7. Click File -> Preferences -> Settings -> Export and add the location of the resourcepacks folder in the minecraft directory to the text box under "Resource Pack Folder Location"
## How to use
### Blockbench Plugin
 1. Make sure the file you're working on is saved and click File -> BAMO Export
 2. Input your chosen block properties
	 * Display Name - The name of the block ingame
	 * [Material](https://minecraft.fandom.com/wiki/Materials) - The type of block that the majority of the new block's properties will be inhereted from
	 * [Blast Resistance](https://minecraft.fandom.com/wiki/Explosion#Blast_resistance) - How resistant to explosions the block is
	 * [Slipperiness](https://www.mcpk.wiki/wiki/Slipperiness) - How much the player slides on the block
	 * Gravity - If checked, the block will fall like sand or gravel
	 * Rotation type - TBA
	 * [Block Sounds](https://minecraft.fandom.com/wiki/Sounds.json#Block_sound_categories) - The vanilla block that the block's sounds will copy
	 * [Luminance](https://minecraft.fandom.com/wiki/Light#Blocks) - How much the block glows
	 * Max Stack Size - The max number of items in a stack of that block
	 * Fireproof - TBA
	 * Creative Tab - Which tab on the creative mode item panel the new block will be added to
 3. Click "Confirm"
### Minecraft Mod
* Ensure that the BAMO resource pack is enabled
* Reboot the game if you add new blocks or change the properties of a block
* Textures and models can be reloaded without rebooting with the shortcut F3+T
* If no BAMO resource pack is shown, make sure you've set the folder setting correctly in Blockbench
## Changelog
### 0.1
Launched initial version of both Minecraft mod and Blockbench plugin with support for Block objects
