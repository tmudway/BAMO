package com.ryytikki.bamo.tools
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block


fun initBlockProperties(data: BlockData) : AbstractBlock.Properties{
    return AbstractBlock.Properties.of(data.material)
        .strength(3.0f, data.blastRes)
        .friction(data.slip)
        .sound(data.sounds)
        .lightLevel{data.lum}
        .noOcclusion()
}

class BamoFunctionProvider(parent: Block, data: BlockData){
    init{

    }
}