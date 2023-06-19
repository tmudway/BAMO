package com.ryytikki.bamo.blocks

import com.ryytikki.bamo.tools.BamoFunctionProvider
import com.ryytikki.bamo.tools.BlockData
import net.minecraft.block.BlockState
import net.minecraft.block.FlowerBlock
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.text.MutableText
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import java.util.*

class BAMOFlowerBlock(prop: Settings, data: BlockData) : FlowerBlock(StatusEffect.byRawId(1), 1, prop){
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

    override fun randomDisplayTick(state: BlockState?, world: World?, pos: BlockPos?, random: Random?) {
        return bamoFunc.randomDisplayTick(world, pos, random)
    }
}