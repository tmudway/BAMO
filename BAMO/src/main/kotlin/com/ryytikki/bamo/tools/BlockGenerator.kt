package com.ryytikki.bamo.tools

import com.ryytikki.bamo.Bamo.ID
import com.ryytikki.bamo.Bamo
import com.ryytikki.bamo.blocks.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderTypeLookup
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraftforge.fml.loading.FMLPaths
import net.minecraft.particles.BasicParticleType
import net.minecraft.particles.ParticleTypes
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate
import java.io.File
import java.nio.file.Files
import java.util.*
import java.util.function.Supplier
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import kotlin.collections.ArrayList
import kotlin.io.path.readText

@Serializable
data class JSONData(
    val displayName : String,                           // Done
    val typeList: List<String>,                         // WIP
    val material : String,                              // Done
    val blastRes : Float,                               // Done
    val slip : Float,                                   // Done
    val gravity : Boolean,                              // Done
    val rotType : String,                               // WIP
    val sounds : String,                                // Done
    val lum : Int,                                      // Done
    val maxStack : Int,                                 // DONE
    val fireproof : Boolean,                            // WIP
    val creativeTab : String,                           // DONE
    val transparency: String = "Solid",                 // DONE
    val hitbox: List<Array<DoubleArray>> = emptyList(), // DONE
    val hitboxBuffer: String = "",
    val blockType: String = "",
    val particleType: String = "",
    val particlePos: DoubleArray = doubleArrayOf(0.0, 0.0, 0.0),
    val particleSpread: DoubleArray = doubleArrayOf(0.0, 0.0, 0.0),
    val particleVel: DoubleArray = doubleArrayOf(0.0, 0.0, 0.0),
//    val lang: String,                                   // WIP
    val nameGenType: String = "old",
)

data class ParticleData(
    val enabled: Boolean,
    val type: BasicParticleType,
    val pos: DoubleArray,
    val spread: DoubleArray,
    val vel: DoubleArray
)

data class BlockData(
    var material: Material,
    var displayName: String,
    var blastRes: Float,
    var slip: Float,
    var sounds: SoundType,
    var lum: Int,
    var fireproof: Boolean,
    var hitbox: List<Array<DoubleArray>>,
    var hitboxBuffer: String,
    var particles: ParticleData,
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

val particleMap = mapOf<String, BasicParticleType>(
    "Ambient Entity Effect" to ParticleTypes.AMBIENT_ENTITY_EFFECT,
    "Angry Villager" to ParticleTypes.ANGRY_VILLAGER,
    "Ash" to ParticleTypes.ASH,
    "Bubble" to ParticleTypes.BUBBLE,
    "Bubble Pop" to ParticleTypes.BUBBLE_POP,
    "Campfire Smoke" to ParticleTypes.CAMPFIRE_COSY_SMOKE,
    "Cloud" to ParticleTypes.CLOUD,
    "Composter" to ParticleTypes.COMPOSTER,
    "Crimson Spore" to ParticleTypes.CRIMSON_SPORE,
    "Crit" to ParticleTypes.CRIT,
    "Damage Indicator" to ParticleTypes.DAMAGE_INDICATOR,
    "Dolphin" to ParticleTypes.DOLPHIN,
    "Dripping Honey" to ParticleTypes.DRIPPING_HONEY,
    "Dripping Lava" to ParticleTypes.DRIPPING_LAVA,
    "Dripping Obsidian Tear" to ParticleTypes.DRIPPING_OBSIDIAN_TEAR,
    "Dripping Water" to ParticleTypes.DRIPPING_WATER,
    "Elder Guardian" to ParticleTypes.ELDER_GUARDIAN,
    "Enchant" to ParticleTypes.ENCHANT,
    "Enchanted Hit" to ParticleTypes.ENCHANTED_HIT,
    "End Rod" to ParticleTypes.END_ROD,
    "Entity Effect" to ParticleTypes.ENTITY_EFFECT,
    "Explosion" to ParticleTypes.EXPLOSION,
    "Falling Honey" to ParticleTypes.FALLING_HONEY,
    "Falling Lava" to ParticleTypes.FALLING_LAVA,
    "Falling Nectar" to ParticleTypes.FALLING_NECTAR,
    "Falling Obsidian Tear" to ParticleTypes.FALLING_OBSIDIAN_TEAR,
    "Falling Water" to ParticleTypes.FALLING_WATER,
    "Firework" to ParticleTypes.FIREWORK,
    "Fishing" to ParticleTypes.FISHING,
    "Flame" to ParticleTypes.FLAME,
    "Flash" to ParticleTypes.FLASH,
    "Happy Villager" to ParticleTypes.HAPPY_VILLAGER,
    "Heart" to ParticleTypes.HEART,
    "Slime Item" to ParticleTypes.ITEM_SLIME,
    "Snowball item" to ParticleTypes.ITEM_SNOWBALL,
    "Landing Honey" to ParticleTypes.LANDING_HONEY,
    "Landing Lava" to ParticleTypes.LANDING_LAVA,
    "Landing Obsidian Tear" to ParticleTypes.LANDING_OBSIDIAN_TEAR,
    "Lava" to ParticleTypes.LAVA,
    "Mycelium" to ParticleTypes.MYCELIUM,
    "Nautilus" to ParticleTypes.NAUTILUS,
    "Note" to ParticleTypes.NOTE,
    "Rain" to ParticleTypes.RAIN,
    "Reverse Portal" to ParticleTypes.REVERSE_PORTAL,
    "Soul" to ParticleTypes.SOUL,
    "Soulfire Flame" to ParticleTypes.SOUL_FIRE_FLAME,
    "Sweep Attack" to ParticleTypes.SWEEP_ATTACK,
    "Totem of Undying" to ParticleTypes.TOTEM_OF_UNDYING,
    "Undewater" to ParticleTypes.UNDERWATER,
    "Warped Spore" to ParticleTypes.WARPED_SPORE,
    "White Ash" to ParticleTypes.WHITE_ASH,
    "Witch" to ParticleTypes.WITCH
)


/** Helper to simplify iterating over zip entries */
private fun ZipFile.forEntries(func: (ZipFile, ZipEntry) -> Unit) = use { zip ->
    val entries = zip.entries()
    while (entries.hasMoreElements()) {
        func(zip, entries.nextElement())
    }
}

@Suppress("OPT_IN_IS_NOT_ENABLED") // ???
@OptIn(ExperimentalSerializationApi::class)
/** Collects all JSON objects in all BAMO packs */
private fun collectJsonObjects(): List<JSONData> {
    val data = ArrayList<JSONData>()
    // Would prefer to use the vanilla resource pack system for this, but that's not usable this early
    BamoPackFinder.packFiles.forEach { (_, file) ->
        if (file.isDirectory) {
            // Collect all JSON objects in the directory
            Files.walk(file.toPath().resolve("objects")).filter(Files::isRegularFile)
                .filter { it.toString().endsWith(".json") }.map { it.readText(Charsets.UTF_8) }
                .map<JSONData>(Json::decodeFromString).forEach(data::add)
        } else {
            // Collect all JSON objects in the zip file
            ZipFile(file).forEntries { zip, entry ->
                val name = entry.name
                if (!entry.isDirectory && name.startsWith("objects/", true) && name.endsWith(".json", true)) {
                    zip.getInputStream(entry).use { stream ->
                        data.add(Json.decodeFromStream(stream))
                    }
                }
            }
        }
    }
    return data
}

object BlockGenerator {
    // use of the new KDeferredRegister
    val BLOCK_REGISTRY = KDeferredRegister(ForgeRegistries.BLOCKS, Bamo.ID)
    val ITEM_REGISTRY = KDeferredRegister(ForgeRegistries.ITEMS, Bamo.ID)

    private val blockData = mutableMapOf<ObjectHolderDelegate<Block>, JSONData>()

    fun generateBlocks() {
        // Loop through all the objects
        collectJsonObjects().forEach { data ->
            // Turn JSON object into block
            println("Generating block: " + data.displayName)

            val block = registerBlockFromJson(data)
            blockData[block] = data

            if (data.typeList.isNotEmpty()){
                data.typeList.forEach{ type ->
                    val chBlock = registerDependantBlockFromJson(data, block, type)
                    blockData[chBlock] = data
                }
            }
        }
    }

    private fun registerBlockFromJson(data: JSONData): ObjectHolderDelegate<Block>{

        var pData = ParticleData((data.particleType != ""), particleMap[data.particleType]?: ParticleTypes.SMOKE, data.particlePos, data.particleSpread, data.particleVel)

        val bData = BlockData((matMap[data.material]?:Material.DIRT), data.displayName, data.blastRes, data.slip,
            (soundsMap[data.sounds] ?: SoundType.GRASS), data.lum, data.fireproof, data.hitbox, data.hitboxBuffer, pData)

        var blockName = ""

        if (data.nameGenType == "old"){
            blockName = data.displayName.replace("\\s+".toRegex(), "").lowercase()
        }else{
            blockName = data.displayName.replace("\\s+".toRegex(), "_").lowercase()
        }

        val block = BLOCK_REGISTRY.registerObject(blockName) {

            when (data.blockType) {
                "Flower" -> BAMOFlowerBlock(initBlockProperties(bData), bData)
                else -> {
                    if (data.gravity) {
                        BAMOFallingBlock(bData)
                    } else {
                        if (data.rotType == "y_axis") {
                            println("Generating Y_Axis block")
                            BAMOHorizontalBlock(initBlockProperties(bData), bData)
                            //}else if (data.rotType == "y_axis_player"){
                            //BAMOHorizontalFaceBlock(initBlockProperties(bData), bData)
                        } else {
                            BAMOBlock(initBlockProperties(bData), bData)
                        }
                    }
                }
            }
        }
        // Register the item version of the block
        ITEM_REGISTRY.registerObject(blockName){
            BlockItem(block.get(), Item.Properties().tab((tabsMap[data.creativeTab]?: ItemGroup.TAB_BUILDING_BLOCKS)).stacksTo(data.maxStack))
        }

        return block
    }

    private fun registerDependantBlockFromJson(data: JSONData, pBlock: ObjectHolderDelegate<Block>, type:String): ObjectHolderDelegate<Block>{
        var pData = ParticleData((data.particleType != ""), particleMap[data.particleType]?: ParticleTypes.SMOKE, data.particlePos, data.particleSpread, data.particleVel)

        val bData = BlockData((matMap[data.material]?:Material.DIRT), data.displayName, data.blastRes, data.slip,
            (soundsMap[data.sounds] ?: SoundType.GRASS), data.lum, data.fireproof, data.hitbox, data.hitboxBuffer, pData)

        var blockName = ""

        if (data.nameGenType == "old"){
            blockName = data.displayName.replace("\\s+".toRegex(), "").lowercase()
        }else{
            blockName = data.displayName.replace("\\s+".toRegex(), "_").lowercase()
        }
        blockName = "$blockName" + "_$type"

        val block = BLOCK_REGISTRY.registerObject(blockName + "_" + type) {
            when(type){
                "stairs" -> {
                    val state: Supplier<BlockState> = Supplier {pBlock.get().defaultBlockState()}
                    BAMOStairsBlock(state, initBlockProperties(bData), bData)
                }
                "slab" -> BAMOSlabBlock(initBlockProperties(bData), bData)
                "wall" -> BAMOWallBlock(initBlockProperties(bData), bData)
                else -> BAMOBlock(initBlockProperties(bData), bData)
            }
        }

        // Register the item version of the block
        ITEM_REGISTRY.registerObject(blockName + "_" + type){
            BlockItem(block.get(), Item.Properties().tab((tabsMap[data.creativeTab]?: ItemGroup.TAB_BUILDING_BLOCKS)).stacksTo(data.maxStack))
        }

        return block
    }

    fun setRenderLayers(){

        val transparencies = mapOf<String, RenderType>(
            "Solid" to RenderType.solid(),
            "Translucent" to RenderType.translucent(),
            "Cutout" to RenderType.cutout(),
            "Cutout (Mipped)" to RenderType.cutoutMipped()
        )

        for ((block, data) in blockData){
            RenderTypeLookup.setRenderLayer(block.get(), (transparencies[data.transparency]?:RenderType.solid()))
        }
    }
}