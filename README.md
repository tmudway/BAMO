# BAMO - Block And Move On
A tool to simplify importing custom assets in Minecraft

Currently only allows you to quickly prototype models in-game, but further functionality is coming

## WARNING - STILL AN EARLY VERSION SO USE AT YOUR OWN RISK
I would **HIGHLY** recommend you backup up any model files before attempting to use this plugin/mod

## Installation
 1. Navigate to the [Releases](https://github.com/tmudway/BAMO/releases) page and find the latest (topmost) release of BAMO
 2. Download the BAMO.jar mod file and move it to the mods folder in your minecraft directory
 3. Download [Kotlin for Forge 1.16.0](https://www.curseforge.com/minecraft/mc-mods/kotlin-for-forge/files/3527736) and move it to the same directory
 4. Download the BAMO.js plugin file
 5. Open Blockbench, click File -> Plugins -> Load Plugins from File
 6. Select the BAMO.js file you just downloaded
 7. Click File -> Preferences -> Settings -> Export and add the location of the minecraft directory to the text box under "Minecraft Folder Location"
## How to use
### Blockbench Plugin
 1. Make sure the file you're working on is saved and click File -> BAMO Export
 2. Input the name you want to give your block". **PLEASE NOTE THIS NAME MUST BE UNIQUE AND WILL OVERRIDE ANY EXISTING EXPORTED FILES OF THE SAME NAME IN YOUR TEST PACK**

** This is the only required step to export a block. Clicking the export button at any point from this step onwards will export a block with the previously input settings (their default values if not set). This means that once you've set the export properties once for a file, you can simply press the export button without having to set them again for that file. This works until you load another file, move to another file's tab or close Blockbench **

 3. Select the type of block you want to make"
	 * Custom Blocks can be any shape and texture combination
	 * Standard Blocks are 1x1 cubes with identical textures on all faces. These can be used to make stairs, walls, and slabs
 4. If you selected Standard Block, select which variant blocks you wish to create and specify what texture each face should use
 5. Input the basic physical properties for the block
 	 * [Material](https://minecraft.fandom.com/wiki/Materials) - The type of block that the majority of the new block's properties will be inhereted from
	 * [Block Sounds](https://minecraft.fandom.com/wiki/Sounds.json#Block_sound_categories) - The vanilla block that the block's sounds will copy
	 * Transparency - The transparency style of the block
	 * Gravity - If checked, the block will fall like sand or gravel
 6. Input the advanced physical properties for the block
	 * [Luminance](https://minecraft.fandom.com/wiki/Light#Blocks) - How much the block glows	 
	 * [Blast Resistance](https://minecraft.fandom.com/wiki/Explosion#Blast_resistance) - How resistant to explosions the block is
	 * [Slipperiness](https://www.mcpk.wiki/wiki/Slipperiness) - How much the player slides on the block
	 * Gravity - If checked, the block will fall like sand or gravel
	 * Fireproof - TBA
7. Input the other block properties
	 * Max Stack Size - The max number of items in a stack of that block
	 * Creative Tab - Which tab on the creative mode item panel the new block will be added to
	 * Rotation type - TBA
 5. Click "Export"
### Minecraft Mod
* Add the bamo .jar file to your minecraft mods directory
* Reboot the game if you add new blocks or change the properties of a block
* Textures and models can be reloaded without rebooting with the shortcut F3+T
* If no BAMO resource pack is shown, make sure you've set the folder setting correctly in Blockbench
## Changelog
### 0.3.0
* Added collision support
* Added rotateable blocks (currently y-axis only)
* Fixed block names by overriding function that returns it (vs using a lang file)
### 0.2.0
* Continued developing plugin UI
* Revamped mod to use custom block classes
* Added support for rapid single-texture block export
* Added variant options for stairs, walls, and slabs
* Reworked resourcepack system to instead load data from either folder or .zip file in bamopacks folder
### 0.1.3
* Revamped blockbench plugin UI
* Created placeholder settings for future export options
### 0.1.2
* Fixed issue with advanced mode on blockbench plugin not showing correctly
* Added transparency option
### 0.1.1
* Fixed bug causing files to fail to load when BAMO was installed
* Started conversion of BAMO to a wizard based interface
* Added Instant Export function
### 0.1.0
* Launched initial version of both Minecraft mod and Blockbench plugin with support for Block objects