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
import net.minecraft.text.TranslatableText
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

    override fun register(infoConsumer: Consumer<ResourcePackProfile>, infoFactory: Factory) {
        // Create the pack info objects for each BAMO pack
        val compiledInfo = packFiles.mapNotNull { (name, file) ->
            val packSupplier = { if (file.isDirectory) DirectoryResourcePack(file) else ZipResourcePack(file) }
            ResourcePackProfile.of(name, true, packSupplier, infoFactory, TOP, PACK_SOURCE_NONE)
        }
        // Skip merging on the server since it doesn't matter there
        if (FabricLoader.getInstance().environmentType == EnvType.SERVER) return compiledInfo.forEach(infoConsumer::accept)
        // open just calls the supplier so... this is probably fine, right?
        val packs = compiledInfo.map { it.createResourcePack() }
        // Since the merged pack is programmatic, we can always assume current version
        val version = SharedConstants.getGameVersion().getPackVersion(PackType.RESOURCE)
        val metadata = PackResourceMetadata(TranslatableText("$ID.resources.$DIR_NAME", packs.size), version)
        val packSupplier = { MergedResourcePack(DIR_NAME, "BAMO Pack Resources", metadata, packs) }
        val packInfo = ResourcePackProfile.of(DIR_NAME, true, packSupplier, infoFactory, TOP, PACK_SOURCE_NONE)
        // Should never be null, but still
        if (packInfo != null) infoConsumer.accept(packInfo) else LOGGER.error("Failed to load merged BAMO pack")
    }

    /** Adds this pack finder to the server pack repo */
    fun addToDataPacks(server: MinecraftServer) {
        val packRepo = server.dataPackManager
        (packRepo as ResourcePackManagerAccessor).providers.add(BamoPackFinder)
        packRepo.scanPacks()
        server.reloadResources(packRepo.enabledNames).get()
    }

    /** Adds this pack finder to the client pack repo */
    fun addToResourcePacks() = (MinecraftClient.getInstance().resourcePackManager as ResourcePackManagerAccessor).providers.add(BamoPackFinder)
}