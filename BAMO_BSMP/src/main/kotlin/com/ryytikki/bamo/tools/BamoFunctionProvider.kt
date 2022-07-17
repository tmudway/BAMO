package com.ryytikki.bamo.tools
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes

fun initBlockProperties(data: BlockData) : AbstractBlock.Settings{
    return AbstractBlock.Settings.of(data.material)
        .strength(3.0f, data.blastRes)
        .slipperiness(data.slip)
        .sounds(data.sounds)
        .luminance(){data.lum}
        .nonOpaque()
}

class BamoFunctionProvider(parent: Block, data: BlockData){

    private var baseNorth = Block.createCuboidShape(0.0,0.0,0.0,0.0,0.0,0.0)
    private var baseEast = Block.createCuboidShape(0.0,0.0,0.0,0.0,0.0,0.0)
    private var baseSouth = Block.createCuboidShape(0.0,0.0,0.0,0.0,0.0,0.0)
    private var baseWest = Block.createCuboidShape(0.0,0.0,0.0,0.0,0.0,0.0)

    private var parent:Block
    private var data:BlockData

    init{

        this.parent = parent
        this.data = data

        if (data.hitbox.isEmpty()){
            baseNorth = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
        }else{
            data.hitbox.forEach{box->
                val hbNorth = Block.createCuboidShape(box[0][0], box[0][1], box[0][2], box[1][0], box[1][1], box[1][2])
                val hbEast = Block.createCuboidShape(16.0 - box[1][2], box[0][1], box[0][0], 16.0 - box[0][2], box[1][1], box[1][0])
                val hbSouth = Block.createCuboidShape(16.0 - box[1][0], box[0][1], 16.0 - box[1][2], 16.0 - box[0][0], box[1][1], 16.0 - box[0][2])
                val hbWest =  Block.createCuboidShape(box[0][2], box[0][1], 16.0 - box[1][0], box[1][2], box[1][1], 16.0 - box[0][0])
                baseNorth = VoxelShapes.union(baseNorth, hbNorth)
                baseEast = VoxelShapes.union(baseEast, hbEast)
                baseSouth = VoxelShapes.union(baseSouth, hbSouth)
                baseWest = VoxelShapes.union(baseWest, hbWest)
            }
        }
    }

    public fun getBoundingBox(): VoxelShape{
        return baseNorth
    }

    public fun getBoundingBoxFromFacing(facing:Direction): VoxelShape{
        if (facing == Direction.NORTH){
            return baseNorth
        }else if (facing == Direction.EAST){
            return baseEast
        }else if (facing == Direction.SOUTH){
            return baseSouth
        }else{
            return baseWest
        }
    }

    public fun getName(): MutableText {
        return LiteralText(data.displayName)
    }

    public fun getDescID(): String {
        return data.displayName
    }
}