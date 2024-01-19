package com.ryytikki.bamo.tools

import com.mojang.bridge.game.PackType
import com.ryytikki.bamo.ID
import com.ryytikki.bamo.LOGGER
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.mixin.resource.loader.ResourcePackManagerAccessor
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.SharedConstants
import net.minecraft.client.MinecraftClient
import net.minecraft.resource.ZipResourcePack
import net.minecraft.resource.DirectoryResourcePack
import net.minecraft.resource.ResourcePackProvider
import net.minecraft.resource.ResourcePackSource.PACK_SOURCE_NONE
import net.minecraft.resource.ResourcePackProfile
import net.minecraft.resource.ResourcePackProfile.Factory
import net.minecraft.resource.ResourcePackProfile.InsertionPosition.TOP
import net.minecraft.resource.metadata.PackResourceMetadata
import net.minecraft.server.MinecraftServer
import net.minecraft.text.Text
import java.io.File
import java.io.FileFilter
import java.util.function.Consumer

object BamoDataPackFinder : ResourcePackProvider {
    private const val DIR_NAME = "bamopacks"
    private val PACKS_DIR = File("${FabricLoader.getInstance().gameDir}/$DIR_NAME")
    private val PACK_FILTER = FileFilter { file ->
        val isZip = file.isFile && file.name.endsWith(".zip")
        val isDir = file.isDirectory && File(file, "pack.mcmeta").isFile
        isZip || isDir
    }
    val packFiles by lazy {
        HashMap<String, File>().also { packs ->
            if (PACKS_DIR.isDirectory || PACKS_DIR.mkdir()) {
                PACKS_DIR.listFiles(PACK_FILTER)?.forEach { packs["$ID:${it.name}"] = it }
            } else LOGGER.error("Failed to find or create $DIR_NAME directory")
            LOGGER.debug("Found ${packs.size} BAMO packs")
        }
    }

    override fun register(infoConsumer: Consumer<ResourcePackProfile>, infoFactory: Factory) {
        // Create the pack info objects for each BAMO pack
        val compiledInfo = packFiles.mapNotNull { (name, file) ->
            val packSupplier = { if (file.isDirectory) DirectoryResourcePack(file) else ZipResourcePack(file) }
            ResourcePackProfile.of(name, true, packSupplier, infoFactory, TOP, PACK_SOURCE_NONE)
        }
        // Return the unmerged list to enable tag support
        return compiledInfo.forEach(infoConsumer::accept)
    }

    /** Adds this pack finder to the server pack repo */
    fun addToDataPacks(server: MinecraftServer) {
        val packRepo = server.dataPackManager
        (packRepo as ResourcePackManagerAccessor).providers.add(BamoDataPackFinder)
        packRepo.scanPacks()
        server.reloadResources(packRepo.enabledNames).get()
    }
}