package com.ryytikki.bamo.blocks

import com.ryytikki.bamo.tools.BamoFunctionProvider
import com.ryytikki.bamo.tools.BlockData

import net.minecraft.block.BlockState
import net.minecraft.block.StairsBlock
import net.minecraft.block.WallBlock
import net.minecraft.util.text.IFormattableTextComponent
import java.util.function.Supplier

class BAMOWallBlock(prop:Properties, data: BlockData) : WallBlock(prop){

    private val bamoFunc:BamoFunctionProvider

    init {
        bamoFunc = BamoFunctionProvider(this, data)
    }

    override fun getName(): IFormattableTextComponent {
        return bamoFunc.getName()
    }

    override fun getDescriptionId(): String {
        return bamoFunc.getDescID()
    }
}