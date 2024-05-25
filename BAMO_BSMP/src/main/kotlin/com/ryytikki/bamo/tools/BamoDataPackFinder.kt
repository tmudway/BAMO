package com.ryytikki.bamo.tools

import com.ryytikki.bamo.ID
import com.ryytikki.bamo.LOGGER
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.resource.DirectoryResourcePack
import net.minecraft.resource.ResourcePackProfile
import net.minecraft.resource.ResourcePackProfile.InsertionPosition.TOP
import net.minecraft.resource.ResourcePackProvider
import net.minecraft.resource.ResourcePackSource.NONE
import net.minecraft.resource.ResourceType
import net.minecraft.resource.ZipResourcePack
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

    override fun register(infoConsumer: Consumer<ResourcePackProfile>) {
        // Create the pack info objects for each BAMO pack
        val compiledInfo = packFiles.mapNotNull { (name, file) ->
            val packSupplier = { _: String -> if (file.isDirectory) DirectoryResourcePack(name, file.toPath(), false) else ZipResourcePack(name, file, false) }
            ResourcePackProfile.create(name, Text.literal(name), true, packSupplier, ResourceType.SERVER_DATA, TOP, NONE)
        }
        // Return the unmerged list to enable tag support
        return compiledInfo.forEach(infoConsumer::accept)
    }

    /** Adds this pack finder to the server pack repo */
    fun addToDataPacks(server: MinecraftServer) {
        val packRepo = server.dataPackManager
        packRepo.providers.add(BamoDataPackFinder)
        packRepo.scanPacks()
        server.reloadResources(packRepo.enabledNames).get()
    }
}