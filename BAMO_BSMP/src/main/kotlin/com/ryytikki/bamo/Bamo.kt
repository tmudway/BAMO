package com.ryytikki.bamo

import com.ryytikki.bamo.tools.BamoPackFinder
import com.ryytikki.bamo.tools.BlockGenerator
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


/**
 * Main mod class. Should be an `object` declaration annotated with `@Mod`.
 * The modid should be declared in this object and should match the modId entry
 * in mods.toml.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */

const val ID: String = "bamo"
// the logger for our mod
val LOGGER: Logger = LogManager.getLogger()

@Suppress("unused")
fun init(){
    // the modid of our mod



    BlockGenerator.generateBlocks()


        // Add our resource pack finder (FMLClientSetupEvent is too late to do this)
        //DistExecutor.safeRunWhenOn(Dist.CLIENT) { SafeRunnable { BamoPackFinder.addToResourcePacks() } }
}
    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
    /*private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client...")
        BlockGenerator.setRenderLayers()
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerStarting(event: FMLServerStartingEvent) {
        BamoPackFinder.addToDataPacks(event.server)
    }*/
