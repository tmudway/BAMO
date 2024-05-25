package com.ryytikki.bamo.tools

import com.ryytikki.bamo.ID
import com.ryytikki.bamo.LOGGER
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.SharedConstants
import net.minecraft.client.MinecraftClient
import net.minecraft.resource.*
import net.minecraft.resource.ResourcePackProfile.InsertionPosition.TOP
import net.minecraft.resource.ResourcePackSource.NONE
import net.minecraft.resource.metadata.PackResourceMetadata
import net.minecraft.server.MinecraftServer
import net.minecraft.text.Text
import java.io.File
import java.io.FileFilter
import java.util.function.Consumer

object BamoPackFinder : ResourcePackProvider {
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
            val packType = if (FabricLoader.getInstance().environmentType == EnvType.SERVER) ResourceType.SERVER_DATA else ResourceType.CLIENT_RESOURCES
            val packSupplier = { _: String -> if (file.isDirectory) DirectoryResourcePack(name, file.toPath(), false) else ZipResourcePack(name, file, false) }
            ResourcePackProfile.create(name, Text.literal(name), true, packSupplier, packType, TOP, NONE)
        }
        // Skip merging on the server since it doesn't matter there
        if (FabricLoader.getInstance().environmentType == EnvType.SERVER) return compiledInfo.forEach(infoConsumer::accept)
        // open just calls the supplier so... this is probably fine, right?
        val packs = compiledInfo.map { it.createResourcePack() }
        // Since the merged pack is programmatic, we can always assume current version
        val version = SharedConstants.getGameVersion().getResourceVersion(ResourceType.CLIENT_RESOURCES)
        val metadata = PackResourceMetadata(Text.translatable("$ID.resources.$DIR_NAME", packs.size), version)
        val packSupplier = { _: String -> MergedResourcePack(DIR_NAME, "BAMO Pack Resources", metadata, packs) }
        val packInfo = ResourcePackProfile.create(DIR_NAME, Text.literal(DIR_NAME), true, packSupplier, ResourceType.CLIENT_RESOURCES, TOP, NONE)
        // Should never be null, but still
        if (packInfo != null) infoConsumer.accept(packInfo) else LOGGER.error("Failed to load merged BAMO pack")
    }

    /** Adds this pack finder to the server pack repo */
    fun addToDataPacks(server: MinecraftServer) {
        val packRepo = server.dataPackManager
        packRepo.providers.add(BamoPackFinder)
        packRepo.scanPacks()
        server.reloadResources(packRepo.enabledNames).get()
    }

    /** Adds this pack finder to the client pack repo */
    fun addToResourcePacks() = MinecraftClient.getInstance().resourcePackManager.providers.add(BamoPackFinder)
}