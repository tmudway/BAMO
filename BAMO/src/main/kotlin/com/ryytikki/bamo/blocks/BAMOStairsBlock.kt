package com.ryytikki.bamo.blocks

import com.ryytikki.bamo.tools.BamoFunctionProvider
import com.ryytikki.bamo.tools.BlockData

import net.minecraft.block.BlockState
import net.minecraft.block.StairsBlock
import java.util.function.Supplier

class BAMOStairsBlock(state: Supplier<BlockState>, prop:Properties, data: BlockData) : StairsBlock(state, prop){

    private val bamoFunc:BamoFunctionProvider

    init {
        bamoFunc = BamoFunctionProvider(this, data)
    }
}