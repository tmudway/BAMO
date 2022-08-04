package com.ryytikki.bamo.blocks

import com.ryytikki.bamo.tools.BamoFunctionProvider
import com.ryytikki.bamo.tools.BlockData
import net.minecraft.block.FlowerBlock
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.text.MutableText

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
}