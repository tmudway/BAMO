package com.ryytikki.bamo.tools

import com.ryytikki.bamo.Bamo
import com.ryytikki.bamo.Bamo.LOGGER
import net.minecraft.client.Minecraft
import net.minecraft.resources.*
import net.minecraft.resources.IPackNameDecorator.DEFAULT
import net.minecraft.resources.ResourcePackInfo.IFactory
import net.minecraft.resources.ResourcePackInfo.Priority.TOP
import net.minecraft.server.MinecraftServer
import net.minecraftforge.fml.loading.FMLPaths
import java.io.File
import java.io.FileFilter
import java.util.function.Consumer

object BamoPackFinder : IPackFinder {
    private val PACKS_DIR = File(FMLPaths.GAMEDIR.get().toString() + "/bamopacks")
    private val PACK_FILTER = FileFilter { file ->
        val isZip = file.isFile && file.name.endsWith(".zip")
        val isDir = file.isDirectory && File(file, "pack.mcmeta").isFile
        isZip || isDir
    }

    override fun loadPacks(packInfos: Consumer<ResourcePackInfo>, infoFactory: IFactory) =
        findPacks().forEach { (name, file) ->
            // Largely copied over from FolderPackFinder, with some adjustments
            // TODO: Compile all packs into one info object, like Forge does for mods?
            val packSupplier: () -> IResourcePack = { if (file.isDirectory) FolderPack(file) else FilePack(file) }
            val packInfo = ResourcePackInfo.create(name, true, packSupplier, infoFactory, TOP, DEFAULT)
            if (packInfo != null) packInfos.accept(packInfo)
        }

    /** Finds and compiles all BAMO packs into a map */
    fun findPacks(): Map<String, File> {
        val packs = HashMap<String, File>()
        if (PACKS_DIR.isDirectory || PACKS_DIR.mkdir()) {
            PACKS_DIR.listFiles(PACK_FILTER)?.forEach { packs["${Bamo.ID}:${it.name}"] = it }
        } else LOGGER.error("Failed to find or create bamopacks directory")
        return packs
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