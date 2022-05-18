package com.ryytikki.bamo.tools
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.state.DirectionProperty
import net.minecraft.util.Direction
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.math.shapes.VoxelShapes
import net.minecraft.util.text.IFormattableTextComponent
import net.minecraft.util.text.StringTextComponent


fun initBlockProperties(data: BlockData) : AbstractBlock.Properties{
    return AbstractBlock.Properties.of(data.material)
        .strength(3.0f, data.blastRes)
        .friction(data.slip)
        .sound(data.sounds)
        .lightLevel{data.lum}
        .noOcclusion()
}

class BamoFunctionProvider(parent: Block, data: BlockData){

    private var baseNorth = Block.box(0.0,0.0,0.0,0.0,0.0,0.0)
    private var baseEast = Block.box(0.0,0.0,0.0,0.0,0.0,0.0)
    private var baseSouth = Block.box(0.0,0.0,0.0,0.0,0.0,0.0)
    private var baseWest = Block.box(0.0,0.0,0.0,0.0,0.0,0.0)

    private var parent:Block
    private var data:BlockData

    init{

        this.parent = parent
        this.data = data

        if (data.hitbox.isEmpty()){
            baseNorth = Block.box(0.0, 16.0, 0.0, 16.0, 0.0, 16.0)
        }else{
            data.hitbox.forEach{box->
                val hbNorth = Block.box(box[0][0], box[0][1], box[0][2], box[1][0], box[1][1], box[1][2])
                val hbEast = Block.box(16.0 - box[1][2], box[0][1], box[0][0], 16.0 - box[0][2], box[1][1], box[1][0])
                val hbSouth = Block.box(16.0 - box[1][0], box[0][1], 16.0 - box[1][2], 16.0 - box[0][0], box[1][1], 16.0 - box[0][2])
                val hbWest =  Block.box(box[0][2], box[0][1], 16.0 - box[1][0], box[1][2], box[1][1], 16.0 - box[0][0])
                baseNorth = VoxelShapes.or(baseNorth, hbNorth)
                baseEast = VoxelShapes.or(baseEast, hbEast)
                baseSouth = VoxelShapes.or(baseSouth, hbSouth)
                baseWest = VoxelShapes.or(baseWest, hbWest)
                baseNorth.optimize()
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

    public fun getName(): IFormattableTextComponent {
        return StringTextComponent(data.displayName)
    }

    public fun getDescID(): String {
        return data.displayName
    }
}