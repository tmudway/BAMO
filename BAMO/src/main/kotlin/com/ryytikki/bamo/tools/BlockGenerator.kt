package com.ryytikki.bamo.tools

import com.ryytikki.bamo.Bamo
import com.ryytikki.bamo.blocks.BAMOBlock
import com.ryytikki.bamo.blocks.BAMOFallingBlock

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraftforge.registries.ForgeRegistries
import net.minecraft.client.renderer.RenderTypeLookup
import net.minecraft.client.renderer.RenderType
import net.minecraftforge.fml.loading.FMLPaths
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Serializable
data class JSONData(
    val displayName : String,             // Done
    val material : String,                // Done
    val blastRes : Float,                 // Done
    val slip : Float,                     // Done
    val gravity : Boolean,                // Done
    val rotType : String,                 // WIP
    val sounds : String,                  // Done
    val lum : Int,                        // Done
    val maxStack : Int,                   // DONE
    val fireproof : Boolean,              // WIP
    val creativeTab : String,             // DONE
    val transparency: String = "Solid",   // DONE
)

data class BlockData(
    var material: Material,
    var displayName: String,
    var blastRes: Float,
    var slip: Float,
    var sounds: SoundType,
    var lum: Int,
    var fireproof: Boolean,
)

val matMap = mapOf<String, Material>(
    "Air" to Material.AIR,
    "Bamboo" to Material.BAMBOO,
    "Bamboo Sapling" to Material.BAMBOO_SAPLING,
    "Barrier" to Material.BARRIER,
    "Bubble Column" to Material.BUBBLE_COLUMN,
    "Buildable Glass" to Material.BUILDABLE_GLASS,
    "Cactus" to Material.CACTUS,
    "Cake" to Material.CAKE,
    "Clay" to Material.CLAY,
    "Coral" to Material.CORAL,
    "Cloth Decoration" to Material.CLOTH_DECORATION,
    "Decoration" to Material.DECORATION,
    "Dirt" to Material.DIRT,
    "Egg" to Material.EGG,
    "Explosive" to Material.EXPLOSIVE,
    "Fire" to Material.FIRE,
    "Glass" to Material.GLASS,
    "Grass" to Material.GRASS,
    "Heavy Metal" to Material.HEAVY_METAL,
    "Ice" to Material.ICE,
    "Ice Solid" to Material.ICE_SOLID,
    "Lava" to Material.LAVA,
    "Leaves" to Material.LEAVES,
    "Metal" to Material.METAL,
    "Nether Wood" to Material.NETHER_WOOD,
    "Piston" to Material.PISTON,
    "Plant" to Material.PLANT,
    "Portal" to Material.PORTAL,
    "Replaceable Plant" to Material.REPLACEABLE_PLANT,
    "Replaceable Fireproof Plant" to Material.REPLACEABLE_FIREPROOF_PLANT,
    "Replaceable Water Plant" to Material.REPLACEABLE_WATER_PLANT,
    "Sand" to Material.SAND,
    "Shulker Shell" to Material.SHULKER_SHELL,
    "Snow" to Material.SNOW,
    "Sponge" to Material.SPONGE,
    "Stone" to Material.STONE,
    "Structural Air" to Material.STRUCTURAL_AIR,
    "Top Snow" to Material.TOP_SNOW,
    "Vegetable" to Material.VEGETABLE,
    "Water" to Material.WATER,
    "Water Plant" to Material.WATER_PLANT,
    "Web" to Material.WEB,
    "Wood" to Material.WOOD,
    "Wool" to Material.WOOL
)

val soundsMap = mapOf<String, SoundType>(
    "Grass" to SoundType.GRASS,
    "Glass" to SoundType.GLASS,
    "Gravel" to SoundType.GRAVEL,
    "Honey" to SoundType.HONEY_BLOCK,
    "Metal" to SoundType.METAL,
    "Sand" to SoundType.SAND,
    "Snow" to SoundType.SNOW,
    "Stone" to SoundType.STONE,
    "Wool" to SoundType.WOOL,
    "Wood" to SoundType.WOOD,
    "Ancient Debris" to SoundType.ANCIENT_DEBRIS,
    "Anvil" to SoundType.ANVIL,
    "Bamboo" to SoundType.BAMBOO,
    "Bamboo Sapling" to SoundType.BAMBOO_SAPLING,
    "Basalt" to SoundType.BASALT,
    "Bone Brick" to SoundType.BONE_BLOCK,
    "Chain" to SoundType.CHAIN,
    "Coral Block" to SoundType.CORAL_BLOCK,
    "Crop" to SoundType.CROP,
    "Fungus" to SoundType.FUNGUS,
    "Gilded Blackstone" to SoundType.GILDED_BLACKSTONE,
    "Hard Crop" to SoundType.HARD_CROP,
    "Ladder" to SoundType.LADDER,
    "Lantern" to SoundType.LANTERN,
    "Lily Pad" to SoundType.LILY_PAD,
    "Lodestone" to SoundType.LODESTONE,
    "Netherite Block" to SoundType.NETHERITE_BLOCK,
    "Netherrack" to SoundType.NETHERRACK,
    "Nether Bricks" to SoundType.NETHER_BRICKS,
    "Nether Gold Ore" to SoundType.NETHER_GOLD_ORE,
    "Nether Ore" to SoundType.NETHER_ORE,
    "Nether Sprouts" to SoundType.NETHER_SPROUTS,
    "Nether Wart" to SoundType.NETHER_WART,
    "Nylium" to SoundType.NYLIUM,
    "Roots" to SoundType.ROOTS,
    "Scaffolding" to SoundType.SCAFFOLDING,
    "Shroomlight" to SoundType.SHROOMLIGHT,
    "Slime Block" to SoundType.SLIME_BLOCK,
    "Soul Sand" to SoundType.SOUL_SAND,
    "Stem" to SoundType.STEM,
    "Sweet Berry Bush" to SoundType.SWEET_BERRY_BUSH,
    "Twisting Vines" to SoundType.TWISTING_VINES,
    "Vine" to SoundType.VINE,
    "Wart Block" to SoundType.WART_BLOCK,
    "Weeping Vines" to SoundType.WEEPING_VINES,
    "Wet Grass" to SoundType.WET_GRASS
)

val tabsMap = mapOf<String, ItemGroup>(
    "Building Blocks" to ItemGroup.TAB_BUILDING_BLOCKS,
    "Brewing" to ItemGroup.TAB_BREWING,
    "Combat" to ItemGroup.TAB_COMBAT,
    "Food" to ItemGroup.TAB_FOOD,
    "Decorations" to ItemGroup.TAB_DECORATIONS,
    //"Materials" to ItemGroup.TAB_MATERIALS,
    "Misc" to ItemGroup.TAB_MISC,
    "Redstone" to ItemGroup.TAB_REDSTONE,
    "Tools" to ItemGroup.TAB_TOOLS,
    "Transportation" to ItemGroup.TAB_TRANSPORTATION
)

// Get list of JSON files in the bamoFiles folder
fun getJSONPaths() : MutableList<String>{
    val JSONPaths = Paths.get(FMLPaths.GAMEDIR.get().toString(), "/resourcepacks/bamo/data")
    if (Files.exists((JSONPaths))) {
        val paths: MutableList<String> = ArrayList()
        Files.walk(JSONPaths).filter { item -> Files.isRegularFile(item) }
            .filter { item -> item.toString().endsWith(".json") }
            .forEach { paths.add(it.toString()) }
        return paths
    }
    else{
        return Collections.emptyList()
    }
}

object BlockGenerator {
    // use of the new KDeferredRegister
    val BLOCK_REGISTRY = KDeferredRegister(ForgeRegistries.BLOCKS, Bamo.ID)
    val ITEM_REGISTRY = KDeferredRegister(ForgeRegistries.ITEMS, Bamo.ID)

    private val blockData = mutableMapOf<ObjectHolderDelegate<Block>, JSONData>()

    fun generateBlocks(){
        val paths = getJSONPaths()

        // Loop through all the files
        paths.forEach{

            // Extract and deserialize JSON data
            val txt : String = File(it).readText(Charsets.UTF_8)
            val data = Json.decodeFromString<JSONData>(txt)

            // Turn JSON data into object
            println(data.displayName)

            val bData = BlockData((matMap[data.material]?:Material.DIRT), data.displayName, data.blastRes, data.slip,
                                  (soundsMap[data.sounds] ?: SoundType.GRASS), data.lum, data.fireproof)

            val block = BLOCK_REGISTRY.registerObject(data.displayName) {
                if (data.gravity){
                    BAMOFallingBlock(bData)
                }else{
                    BAMOBlock(bData)
                }
            }

            // Register the item version of the block
            ITEM_REGISTRY.registerObject(data.displayName){
                BlockItem(block.get(), Item.Properties().tab(tabsMap[data.creativeTab]).stacksTo(data.maxStack))
            }
            blockData[block] = data
        }
    }

    fun setRenderLayers(){

        val transparencies = mapOf<String, RenderType>(
            "Solid" to RenderType.solid(),
            "Translucent" to RenderType.translucent(),
            "Cutout" to RenderType.cutout(),
            "Cutout (Mipped)" to RenderType.cutoutMipped()
        )

        for ((block, data) in blockData){
            RenderTypeLookup.setRenderLayer(block.get(), transparencies[data.transparency])
        }
    }
}