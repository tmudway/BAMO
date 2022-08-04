package com.ryytikki.bamo.blocks

import com.ryytikki.bamo.tools.BamoFunctionProvider
import com.ryytikki.bamo.tools.BlockData
import com.ryytikki.bamo.tools.initFlowerBlockProperties
import net.minecraft.block.FlowerBlock
import net.minecraft.potion.Effects
import net.minecraft.util.text.IFormattableTextComponent

class BAMOFlowerBlock(prop:Properties, data: BlockData) : FlowerBlock(Effects.SLOW_FALLING, 1, initFlowerBlockProperties(data)){

    private val bamoFunc:BamoFunctionProvider

    init {
        bamoFunc = BamoFunctionProvider(this, data)
    }

    /*override fun getShape(state:BlockState, worldIn:IBlockReader, pos:BlockPos, context: ISelectionContext): VoxelShape{
        return bamoFunc.getBoundingBox()
    }*/

    override fun getName(): IFormattableTextComponent {
        return bamoFunc.getName()
    }

    override fun getDescriptionId(): String {
        return bamoFunc.getDescID()
    }
}