package com.ryytikki.bamo.blocks

import com.ryytikki.bamo.tools.BamoFunctionProvider
import com.ryytikki.bamo.tools.BlockData
import com.ryytikki.bamo.tools.initBlockProperties
import net.minecraft.block.FallingBlock
import net.minecraft.util.text.IFormattableTextComponent

class BAMOFallingBlock(data: BlockData) : FallingBlock(initBlockProperties(data)){

    private val bamoFunc:BamoFunctionProvider

    init {
        bamoFunc = BamoFunctionProvider(this, data)
    }

    override fun getName(): IFormattableTextComponent {
        println("Test getName")
        return bamoFunc.getName()
    }

    override fun getDescriptionId(): String {
        println("Test getDescID")
        return bamoFunc.getDescID()
    }
}