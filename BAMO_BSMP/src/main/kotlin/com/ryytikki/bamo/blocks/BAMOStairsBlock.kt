package com.ryytikki.bamo.blocks

import com.ryytikki.bamo.tools.BamoFunctionProvider
import com.ryytikki.bamo.tools.BlockData
import net.minecraft.block.StairsBlock
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.text.MutableText
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.*
import java.util.function.Supplier

class BAMOStairsBlock(state: Supplier<BlockState>, prop: Settings, data: BlockData) : StairsBlock(state.get(), prop){
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

    @Deprecated("Deprecated in Java")
    override fun randomDisplayTick(state: BlockState?, world: World?, pos: BlockPos?, random: Random?) {
        return bamoFunc.randomDisplayTick(world, pos, random)
    }
}