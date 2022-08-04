package com.ryytikki.bamo

import com.ryytikki.bamo.tools.BamoPackFinder
import com.ryytikki.bamo.tools.BlockGenerator
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

const val ID: String = "bamo"
val LOGGER: Logger = LogManager.getLogger()

@Suppress("unused")
fun init() {
    BlockGenerator.generateBlocks()
    ServerLifecycleEvents.SERVER_STARTING.register(BamoPackFinder::addToDataPacks)
}

@Suppress("unused")
fun initClient() {
    BamoPackFinder.addToResourcePacks()
    BlockGenerator.setRenderLayers()
}
