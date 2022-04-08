package com.ryytikki.bamo.tools

import com.ryytikki.bamo.Bamo.ID
import com.ryytikki.bamo.Bamo.LOGGER
import net.minecraft.client.Minecraft
import net.minecraft.resources.FilePack
import net.minecraft.resources.FolderPack
import net.minecraft.resources.IPackFinder
import net.minecraft.resources.IPackNameDecorator.DEFAULT
import net.minecraft.resources.ResourcePackInfo
import net.minecraft.resources.ResourcePackInfo.IFactory
import net.minecraft.resources.ResourcePackInfo.Priority.TOP
import net.minecraft.resources.data.PackMetadataSection
import net.minecraft.server.MinecraftServer
import net.minecraft.util.SharedConstants
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.fml.loading.FMLEnvironment
import net.minecraftforge.fml.loading.FMLPaths
import net.minecraftforge.fml.packs.DelegatingResourcePack
import java.io.File
import java.io.FileFilter
import java.util.function.Consumer

object BamoPackFinder : IPackFinder {
    private const val DIR_NAME = "bamopacks"
    private val PACKS_DIR = File("${FMLPaths.GAMEDIR.get()}/$DIR_NAME")
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

    override fun loadPacks(infoConsumer: Consumer<ResourcePackInfo>, infoFactory: IFactory) {
        // Create the pack info objects for each BAMO pack
        val compiledInfo = packFiles.mapNotNull { (name, file) ->
            val packSupplier = { if (file.isDirectory) FolderPack(file) else FilePack(file) }
            ResourcePackInfo.create(name, true, packSupplier, infoFactory, TOP, DEFAULT)
        }
        // Skip merging on the server since it doesn't matter there
        if (FMLEnvironment.dist.isDedicatedServer) return compiledInfo.forEach(infoConsumer::accept)
        // open just calls the supplier so... this is probably fine, right?
        val packs = compiledInfo.map { it.open() }
        // Since the merged pack is programmatic, we can always assume current version
        val version = SharedConstants.getCurrentVersion().packVersion
        val metadata = PackMetadataSection(TranslationTextComponent("$ID.resources.$DIR_NAME", packs.size), version)
        val packSupplier = { DelegatingResourcePack(DIR_NAME, "BAMO Pack Resources", metadata, packs) }
        val packInfo = ResourcePackInfo.create(DIR_NAME, true, packSupplier, infoFactory, TOP, DEFAULT)
        // Should never be null, but still
        if (packInfo != null) infoConsumer.accept(packInfo) else LOGGER.error("Failed to load merged BAMO pack")
    }

    // TODO: Forge added a better way to do this in 1.17+ via AddPackFindersEvent
    /** Adds this pack finder to the server pack repo */
    fun addToDataPacks(server: MinecraftServer) {
        val packRepo = server.packRepository
        packRepo.addPackFinder(BamoPackFinder)
        packRepo.reload()
        server.reloadResources(packRepo.selectedIds).get()
    }

    // TODO: Forge added a better way to do this in 1.17+ via AddPackFindersEvent
    /** Adds this pack finder to the client pack repo */
    fun addToResourcePacks() = Minecraft.getInstance().resourcePackRepository.addPackFinder(BamoPackFinder)
}