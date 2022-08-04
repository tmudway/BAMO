package com.ryytikki.bamo.blocks

import com.ryytikki.bamo.tools.BamoFunctionProvider
import com.ryytikki.bamo.tools.BlockData
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.SlabBlock
import net.minecraft.text.MutableText
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

class BAMOSlabBlock(prop: Settings, data: BlockData) : SlabBlock(prop){
    private val bamoFunc: BamoFunctionProvider

    init {
        bamoFunc = BamoFunctionProvider(this, data)
    }

    override fun getName(): MutableText {
        return bamoFunc.getName()
    }

    override fun getTranslationKey(): String {
        return bamoFunc.getDescID()
    }
}