package com.ryytikki.bamo.blocks

import com.ryytikki.bamo.tools.BamoFunctionProvider
import com.ryytikki.bamo.tools.BlockData
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.CakeBlock
import net.minecraft.block.ShapeContext
import net.minecraft.state.property.IntProperty
import net.minecraft.state.property.Properties
import net.minecraft.text.MutableText
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

abstract class BAMOCakeBlock(prop: Settings, data: BlockData) : CakeBlock(prop){
    private val bamoFunc: BamoFunctionProvider
    val MAX_BITES:Int = 6
    val BITES: IntProperty = Properties.BITES
    val DEFAULT_COMPARATOR_OUTPUT = getComparatorOutput(0)
    protected val field_31047 = 1.0f
    protected val field_31048 = 2.0f
    protected val BITES_TO_SHAPE: Array<VoxelShape>

    init {
        bamoFunc = BamoFunctionProvider(this, data)
        BITES_TO_SHAPE = Array(MAX_BITES){
            Block.createCuboidShape(0.0,0.0,0.0,1.0,1.0,1.0)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(state: BlockState, worldIn: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape {
        return BITES_TO_SHAPE[state.get(BITES)]
    }

    override fun getName(): MutableText {
        return bamoFunc.getName()
    }

    override fun getTranslationKey(): String {
        return bamoFunc.getDescID()
    }
}