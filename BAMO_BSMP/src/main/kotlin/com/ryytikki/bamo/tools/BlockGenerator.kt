package com.ryytikki.bamo.tools

import com.ibm.icu.text.MessagePattern
import com.ryytikki.bamo.ID
import com.ryytikki.bamo.blocks.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.block.Material
import net.minecraft.client.particle.Particle
import net.minecraft.client.render.RenderLayer
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleType
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.registry.Registry
import java.nio.file.Files
import java.util.function.Supplier
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
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
    val type: DefaultParticleType,
    val pos: DoubleArray,
    val spread: DoubleArray,
    val vel: DoubleArray
)

data class BlockData(
    var material: Material,
    var displayName: String,
    var blastRes: Float,
    var slip: Float,
    var sounds: BlockSoundGroup,
    var lum: Int,
    var fireproof: Boolean,
    var hitbox: List<Array<DoubleArray>>,
    var hitboxBuffer: String,
    var particles: ParticleData,
)

val particleMap = mapOf<String, DefaultParticleType>(
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
    "Dripping Dripstone Lava" to ParticleTypes.DRIPPING_DRIPSTONE_LAVA,
    "Dripping Dripstone Water" to ParticleTypes.DRIPPING_DRIPSTONE_WATER,
    "Dripping Honey" to ParticleTypes.DRIPPING_HONEY,
    "Dripping Lava" to ParticleTypes.DRIPPING_LAVA,
    "Dripping Obsidian Tear" to ParticleTypes.DRIPPING_OBSIDIAN_TEAR,
    "Dripping Water" to ParticleTypes.DRIPPING_WATER,
    "Elder Guardian" to ParticleTypes.ELDER_GUARDIAN,
    "Electric Spark" to ParticleTypes.ELECTRIC_SPARK,
    "Enchant" to ParticleTypes.ENCHANT,
    "Enchanted Hit" to ParticleTypes.ENCHANTED_HIT,
    "End Rod" to ParticleTypes.END_ROD,
    "Entity Effect" to ParticleTypes.ENTITY_EFFECT,
    "Explosion" to ParticleTypes.EXPLOSION,
    "Falling Dripstone Lava" to ParticleTypes.FALLING_DRIPSTONE_LAVA,
    "Falling Dripstone Water" to ParticleTypes.FALLING_DRIPSTONE_WATER,
    "Falling Honey" to ParticleTypes.FALLING_HONEY,
    "Falling Lava" to ParticleTypes.FALLING_LAVA,
    "Falling Nectar" to ParticleTypes.FALLING_NECTAR,
    "Falling Obsidian Tear" to ParticleTypes.FALLING_OBSIDIAN_TEAR,
    "Falling Spore Blossom" to ParticleTypes.FALLING_SPORE_BLOSSOM,
    "Falling Water" to ParticleTypes.FALLING_WATER,
    "Firework" to ParticleTypes.FIREWORK,
    "Fishing" to ParticleTypes.FISHING,
    "Flame" to ParticleTypes.FLAME,
    "Flash" to ParticleTypes.FLASH,
    "Glow" to ParticleTypes.GLOW,
    "Glow Squid Ink" to ParticleTypes.GLOW_SQUID_INK,
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
    "Scrape" to ParticleTypes.SCRAPE,
    "Small Flame" to ParticleTypes.SMALL_FLAME,
    "Snowflake" to ParticleTypes.SNOWFLAKE,
    "Soul" to ParticleTypes.SOUL,
    "Soulfire Flame" to ParticleTypes.SOUL_FIRE_FLAME,
    "Spore Blossom Air" to ParticleTypes.SPORE_BLOSSOM_AIR,
    "Sweep Attack" to ParticleTypes.SWEEP_ATTACK,
    "Totem of Undying" to ParticleTypes.TOTEM_OF_UNDYING,
    "Undewater" to ParticleTypes.UNDERWATER,
    "Warped Spore" to ParticleTypes.WARPED_SPORE,
    "Wax off" to ParticleTypes.WAX_OFF,
    "Wax On" to ParticleTypes.WAX_ON,
    "White Ash" to ParticleTypes.WHITE_ASH,
    "Witch" to ParticleTypes.WITCH
)

val matMap = mapOf<String, Material>(
    "Air" to Material.AIR,
    "Bamboo" to Material.BAMBOO,
    "Bamboo Sapling" to Material.BAMBOO_SAPLING,
    "Barrier" to Material.BARRIER,
    "Bubble Column" to Material.BUBBLE_COLUMN,
    "Buildable Glass" to Material.GLASS,
    "Cactus" to Material.CACTUS,
    "Cake" to Material.CAKE,
    "Clay" to Material.ORGANIC_PRODUCT,
    "Coral" to Material.UNDERWATER_PLANT,
    "Cloth Decoration" to Material.DECORATION,
    "Decoration" to Material.DECORATION,
    "Dirt" to Material.SOIL,
    "Egg" to Material.EGG,
    "Explosive" to Material.TNT,
    "Fire" to Material.FIRE,
    "Glass" to Material.GLASS,
    "Grass" to Material.SOLID_ORGANIC,
    "Heavy Metal" to Material.METAL,
    "Ice" to Material.ICE,
    "Ice Solid" to Material.DENSE_ICE,
    "Lava" to Material.LAVA,
    "Leaves" to Material.LEAVES,
    "Metal" to Material.METAL,
    "Nether Wood" to Material.NETHER_WOOD,
    "Piston" to Material.PISTON,
    "Plant" to Material.PLANT,
    "Portal" to Material.PORTAL,
    "Replaceable Plant" to Material.REPLACEABLE_PLANT,
    "Replaceable Fireproof Plant" to Material.REPLACEABLE_PLANT,
    "Replaceable Water Plant" to Material.REPLACEABLE_UNDERWATER_PLANT,
    "Sand" to Material.AGGREGATE,
    "Shulker Shell" to Material.SHULKER_BOX,
    "Snow" to Material.SNOW_BLOCK,
    "Sponge" to Material.SPONGE,
    "Stone" to Material.STONE,
    "Structural Air" to Material.STRUCTURE_VOID,
    "Top Snow" to Material.SNOW_LAYER,
    "Vegetable" to Material.GOURD,
    "Water" to Material.WATER,
    "Water Plant" to Material.UNDERWATER_PLANT,
    "Web" to Material.COBWEB,
    "Wood" to Material.WOOD,
    "Wool" to Material.WOOL
)

val soundsMap = mapOf<String, BlockSoundGroup>(
    "Grass" to BlockSoundGroup.GRASS,
    "Glass" to BlockSoundGroup.GLASS,
    "Gravel" to BlockSoundGroup.GRAVEL,
    "Honey" to BlockSoundGroup.HONEY,
    "Metal" to BlockSoundGroup.METAL,
    "Sand" to BlockSoundGroup.SAND,
    "Snow" to BlockSoundGroup.SNOW,
    "Stone" to BlockSoundGroup.STONE,
    "Wool" to BlockSoundGroup.WOOL,
    "Wood" to BlockSoundGroup.WOOD,
    "Ancient Debris" to BlockSoundGroup.ANCIENT_DEBRIS,
    "Anvil" to BlockSoundGroup.ANVIL,
    "Bamboo" to BlockSoundGroup.BAMBOO,
    "Bamboo Sapling" to BlockSoundGroup.BAMBOO_SAPLING,
    "Basalt" to BlockSoundGroup.BASALT,
    "Bone Brick" to BlockSoundGroup.BONE,
    "Chain" to BlockSoundGroup.CHAIN,
    "Coral Block" to BlockSoundGroup.CORAL,
    "Crop" to BlockSoundGroup.CROP,
    "Fungus" to BlockSoundGroup.FUNGUS,
    "Gilded Blackstone" to BlockSoundGroup.GILDED_BLACKSTONE,
    "Hard Crop" to BlockSoundGroup.HANGING_ROOTS,
    "Ladder" to BlockSoundGroup.LADDER,
    "Lantern" to BlockSoundGroup.LANTERN,
    "Lily Pad" to BlockSoundGroup.LILY_PAD,
    "Lodestone" to BlockSoundGroup.LODESTONE,
    "Netherite Block" to BlockSoundGroup.NETHERITE,
    "Netherrack" to BlockSoundGroup.NETHERRACK,
    "Nether Bricks" to BlockSoundGroup.NETHER_BRICKS,
    "Nether Gold Ore" to BlockSoundGroup.NETHER_GOLD_ORE,
    "Nether Ore" to BlockSoundGroup.NETHER_ORE,
    "Nether Sprouts" to BlockSoundGroup.NETHER_SPROUTS,
    "Nether Wart" to BlockSoundGroup.NETHER_WART,
    "Nylium" to BlockSoundGroup.NYLIUM,
    "Roots" to BlockSoundGroup.ROOTS,
    "Scaffolding" to BlockSoundGroup.SCAFFOLDING,
    "Shroomlight" to BlockSoundGroup.SHROOMLIGHT,
    "Slime Block" to BlockSoundGroup.SLIME,
    "Soul Sand" to BlockSoundGroup.SOUL_SAND,
    "Stem" to BlockSoundGroup.STEM,
    "Sweet Berry Bush" to BlockSoundGroup.SWEET_BERRY_BUSH,
    "Twisting Vines" to BlockSoundGroup.TUFF ,
    "Vine" to BlockSoundGroup.VINE,
    "Wart Block" to BlockSoundGroup.WART_BLOCK,
    "Weeping Vines" to BlockSoundGroup.WEEPING_VINES,
    "Wet Grass" to BlockSoundGroup.WET_GRASS
)

val tabsMap = mapOf<String, ItemGroup>(
    "Building Blocks" to ItemGroup.BUILDING_BLOCKS,
    "Brewing" to ItemGroup.BREWING,
    "Combat" to ItemGroup.COMBAT,
    "Food" to ItemGroup.FOOD,
    "Decorations" to ItemGroup.DECORATIONS,
    //"Materials" to ItemGroup.TAB_MATERIALS,
    "Misc" to ItemGroup.MISC,
    "Redstone" to ItemGroup.REDSTONE,
    "Tools" to ItemGroup.TOOLS,
    "Transportation" to ItemGroup.TRANSPORTATION
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

    private val blockData = mutableMapOf<Block, JSONData>()

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

    private fun registerBlockFromJson(data: JSONData): Block{


        var pData = ParticleData((data.particleType != ""), particleMap[data.particleType]?: ParticleTypes.SMOKE, data.particlePos, data.particleSpread, data.particleVel)

        val bData = BlockData((matMap[data.material]?:Material.SOIL), data.displayName, data.blastRes, data.slip,
            (soundsMap[data.sounds] ?: BlockSoundGroup.GRASS), data.lum, data.fireproof, data.hitbox, data.hitboxBuffer, pData)

        var blockName = ""

        if (data.nameGenType == "old"){
            blockName = data.displayName.replace("\\s+".toRegex(), "").lowercase()
        }else{
            blockName = data.displayName.replace("\\s+".toRegex(), "_").lowercase()
        }

        blockName = "$ID:$blockName"

        val block:Block

            if (data.blockType == "Flower") {
                block = Registry.register(Registry.BLOCK, blockName, BAMOFlowerBlock(initFlowerBlockProperties(bData), bData))
            }else{
                if (data.gravity) {
                    block = Registry.register(Registry.BLOCK, blockName, BAMOFallingBlock(initBlockProperties(bData), bData))
                } else {
                    if (data.rotType == "y_axis") {
                        block = Registry.register(Registry.BLOCK, blockName, BAMOHorizontalBlock(initBlockProperties(bData), bData))
                    } else {
                        block = Registry.register(Registry.BLOCK, blockName, BAMOBlock(initBlockProperties(bData), bData))
                    }
                }
            }


        // Register the item version of the block
        Registry.register(Registry.ITEM, blockName, BlockItem(block, Item.Settings().group((tabsMap[data.creativeTab]?: ItemGroup.BUILDING_BLOCKS)).maxCount(data.maxStack)))

        return block
    }

    private fun registerDependantBlockFromJson(data: JSONData, pBlock: Block, type:String): Block{

        var pData = ParticleData((data.particleType == ""), particleMap[data.particleType]?: ParticleTypes.AMBIENT_ENTITY_EFFECT, data.particlePos, data.particleSpread, data.particleVel)

        val bData = BlockData((matMap[data.material]?:Material.SOIL), data.displayName + " $type", data.blastRes, data.slip,
            (soundsMap[data.sounds] ?: BlockSoundGroup.GRASS), data.lum, data.fireproof, data.hitbox, data.hitboxBuffer, pData)

        var blockName = ""

        if (data.nameGenType == "old"){
            blockName = data.displayName.replace("\\s+".toRegex(), "").lowercase()
        }else{
            blockName = data.displayName.replace("\\s+".toRegex(), "_").lowercase()
        }

        blockName = "$ID:$blockName" + "_$type"

        val block:Block
        if(type == "stairs"){
            val state: Supplier<BlockState> = Supplier {pBlock.defaultState}
            block = Registry.register(Registry.BLOCK, blockName, BAMOStairsBlock(state, initBlockProperties(bData), bData))
        }else if(type == "slab") {
            block = Registry.register(Registry.BLOCK, blockName, BAMOSlabBlock(initBlockProperties(bData), bData))
        }else if(type == "wall"){
            block = Registry.register(Registry.BLOCK, blockName, BAMOWallBlock(initBlockProperties(bData), bData))
        }else{
            block = Registry.register(Registry.BLOCK, blockName, BAMOBlock(initBlockProperties(bData), bData))
        }

        // Register the item version of the block
        Registry.register(Registry.ITEM, blockName, BlockItem(block, Item.Settings().group((tabsMap[data.creativeTab]?: ItemGroup.BUILDING_BLOCKS)).maxCount(data.maxStack)))

        return block
    }

    fun setRenderLayers(){

        val transparencies = mapOf<String, RenderLayer>(
            "Solid" to RenderLayer.getSolid(),
            "Translucent" to RenderLayer.getTranslucent(),
            "Cutout" to RenderLayer.getCutout(),
            "Cutout (Mipped)" to RenderLayer.getCutoutMipped()
        )

        for ((block, data) in blockData){
            BlockRenderLayerMap.INSTANCE.putBlock(block, (transparencies[data.transparency]?:RenderLayer.getSolid()))
        }
    }
}