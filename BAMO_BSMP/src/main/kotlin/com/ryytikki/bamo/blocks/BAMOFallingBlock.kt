package com.ryytikki.bamo.blocks

import com.ryytikki.bamo.tools.BamoFunctionProvider
import com.ryytikki.bamo.tools.BlockData
import net.minecraft.block.BlockState
import net.minecraft.block.FallingBlock
import net.minecraft.block.ShapeContext
import net.minecraft.text.MutableText
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.*

class BAMOFallingBlock(prop: Settings, data: BlockData) : FallingBlock(prop){
    private val bamoFunc: BamoFunctionProvider

    init {
        bamoFunc = BamoFunctionProvider(this, data)
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(state: BlockState, worldIn: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape {
        return bamoFunc.getBoundingBox()
    }

    override fun getName(): MutableText {
        return bamoFunc.getName()
    }

    override fun getTranslationKey(): String {
        return bamoFunc.getDescID()
    }

    override fun randomDisplayTick(state: BlockState?, world: World?, pos: BlockPos?, random: Random?) {
        return bamoFunc.randomDisplayTick(world, pos, random)
    }
}