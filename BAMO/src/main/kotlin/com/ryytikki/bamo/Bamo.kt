package com.ryytikki.bamo

import com.ryytikki.bamo.tools.BamoDataPackFinder
import com.ryytikki.bamo.tools.BamoPackFinder
import com.ryytikki.bamo.tools.BlockGenerator
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.DistExecutor.SafeRunnable
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS


/**
 * Main mod class. Should be an `object` declaration annotated with `@Mod`.
 * The modid should be declared in this object and should match the modId entry
 * in mods.toml.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@Mod("bamo")
object Bamo {
    // the modid of our mod
    const val ID: String = "bamo"

    // the logger for our mod
    val LOGGER: Logger = LogManager.getLogger()

    init {

        // Register the KDeferredRegister to the mod-specific event bus
        BlockGenerator.generateBlocks()
        BlockGenerator.BLOCK_REGISTRY.register(MOD_BUS)
        BlockGenerator.ITEM_REGISTRY.register(MOD_BUS)

        // usage of the KotlinEventBus
        MOD_BUS.addListener(::onClientSetup)
        FORGE_BUS.addListener(::onServerStarting)

        // Add our resource pack finder (FMLClientSetupEvent is too late to do this)
        DistExecutor.safeRunWhenOn(Dist.CLIENT) { SafeRunnable { BamoPackFinder.addToResourcePacks() } }
    }

    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client...")
        BlockGenerator.setRenderLayers()
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerStarting(event: FMLServerStartingEvent) {
        BamoDataPackFinder.addToDataPacks(event.server)
    }
}