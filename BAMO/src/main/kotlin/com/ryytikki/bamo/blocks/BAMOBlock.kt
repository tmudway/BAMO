package com.ryytikki.bamo.blocks

import com.ryytikki.bamo.tools.BamoFunctionProvider
import com.ryytikki.bamo.tools.BlockData
import com.ryytikki.bamo.tools.initBlockProperties
import net.minecraft.block.Block

class BAMOBlock(data: BlockData) : Block(initBlockProperties(data)){

    private val bamoFunc:BamoFunctionProvider

    init {
        bamoFunc = BamoFunctionProvider(this, data)
    }
}