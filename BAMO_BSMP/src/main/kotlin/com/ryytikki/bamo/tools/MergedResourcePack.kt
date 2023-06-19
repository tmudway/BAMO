package com.ryytikki.bamo.tools

import net.minecraft.resource.AbstractFileResourcePack
import net.minecraft.resource.ResourceNotFoundException
import net.minecraft.resource.ResourcePack
import net.minecraft.resource.ResourceType
import net.minecraft.resource.ResourceType.CLIENT_RESOURCES
import net.minecraft.resource.ResourceType.SERVER_DATA
import net.minecraft.resource.metadata.PackResourceMetadata
import net.minecraft.resource.metadata.ResourceMetadataReader
import net.minecraft.util.Identifier
import java.io.File
import java.io.InputStream
import java.util.function.Predicate

class MergedResourcePack(
    id: String,
    private val packName: String,
    private val packInfo: PackResourceMetadata,
    private val combinedPacks: List<ResourcePack>
) : AbstractFileResourcePack(File(id)) {
    private val resourcePacks = separatePacks(combinedPacks, CLIENT_RESOURCES)
    private val dataPacks = separatePacks(combinedPacks, SERVER_DATA)

    private fun separatePacks(packs: List<ResourcePack>, type: ResourceType): Map<String, List<ResourcePack>> {
        val map = HashMap<String, List<ResourcePack>>()
        for (pack in packs) {
            for (namespace in pack.getNamespaces(type)) {
                (map.computeIfAbsent(namespace) { ArrayList() } as ArrayList).add(pack)
            }
        }
        return map
    }

    private fun getPacks(type: ResourceType, location: Identifier): List<ResourcePack> {
        val packs = when (type) {
            CLIENT_RESOURCES -> resourcePacks
            SERVER_DATA -> dataPacks
        }
        return packs[location.namespace] ?: emptyList()
    }

    override fun getName() = packName

    @Suppress("UNCHECKED_CAST")
    override fun <T> parseMetadata(deserializer: ResourceMetadataReader<T>) =
        if (deserializer.key.equals("pack")) packInfo as T else null

    override fun findResources(
        type: ResourceType,
        namespace: String,
        prefix: String,
        allowedPathPredicate: Predicate<Identifier>
    ) = combinedPacks.flatMap { it.findResources(type, namespace, prefix, allowedPathPredicate) }

    override fun getNamespaces(type: ResourceType) =
        if (type === CLIENT_RESOURCES) resourcePacks.keys else dataPacks.keys

    override fun close() = combinedPacks.forEach(ResourcePack::close)

    override fun openRoot(fileName: String) = throw ResourceNotFoundException(base, fileName)

    override fun openFile(resourcePath: String) = throw ResourceNotFoundException(base, resourcePath)

    override fun containsFile(resourcePath: String) = false

    override fun open(type: ResourceType, location: Identifier): InputStream {
        for (pack in getPacks(type, location)) {
            if (pack.contains(type, location)) {
                return pack.open(type, location)
            }
        }
        throw ResourceNotFoundException(base, "${type.directory}/${location.namespace}/${location.path}")
    }

    override fun contains(type: ResourceType, location: Identifier): Boolean {
        for (pack in getPacks(type, location)) {
            if (pack.contains(type, location)) {
                return true
            }
        }
        return false
    }
}