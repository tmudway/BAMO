package net.fabricmc.example

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Material

// For support join https://discord.gg/v6v4pMv

@Suppress("unused")
fun init() {
    // This code runs as soon as Minecraft is in a mod-load-ready state.
    // However, some things (like resources) may still be uninitialized.
    // Proceed with mild caution.
    println("Hello Fabric world!")
}

