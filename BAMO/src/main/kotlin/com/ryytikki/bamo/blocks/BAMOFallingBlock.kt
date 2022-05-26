package com.ryytikki.bamo.blocks

import com.ryytikki.bamo.tools.BamoFunctionProvider
import com.ryytikki.bamo.tools.BlockData
import com.ryytikki.bamo.tools.initBlockProperties
import net.minecraft.block.BlockState
import net.minecraft.block.FallingBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.text.IFormattableTextComponent
import net.minecraft.world.IBlockReader

class BAMOFallingBlock(data: BlockData) : FallingBlock(initBlockProperties(data)){

    private val bamoFunc:BamoFunctionProvider

    init {
        bamoFunc = BamoFunctionProvider(this, data)
    }

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return bamoFunc.getBoundingBox()
    }

    override fun getName(): IFormattableTextComponent {
        return bamoFunc.getName()
    }

    override fun getDescriptionId(): String {
        return bamoFunc.getDescID()
    }
}