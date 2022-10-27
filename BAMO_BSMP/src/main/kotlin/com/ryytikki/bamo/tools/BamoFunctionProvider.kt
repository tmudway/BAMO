package com.ryytikki.bamo.tools
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.Material
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.World
import java.util.*

fun initBlockProperties(data: BlockData) : AbstractBlock.Settings{
    return AbstractBlock.Settings.of(data.material)
        .strength(3.0f, data.blastRes)
        .slipperiness(data.slip)
        .sounds(data.sounds)
        .luminance(){data.lum}
        .nonOpaque()
}

fun initFlowerBlockProperties(data:BlockData) : AbstractBlock.Settings{
    return AbstractBlock.Settings.copy(Blocks.DANDELION)
        .strength(3.0f, data.blastRes)
        .slipperiness(data.slip)
        .sounds(data.sounds)
        .luminance(){data.lum}
        .nonOpaque()
}

fun initCakeBlockProperties(data:BlockData) : AbstractBlock.Settings{
    return AbstractBlock.Settings.copy(Blocks.CAKE)
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
            if (data.hitboxBuffer != "") {
                // Generate a hitbox using the min axis values + buffer
                var minHitbox = doubleArrayOf(16.0, 16.0, 16.0)
                var maxHitbox = doubleArrayOf(0.0, 0.0, 0.0)
                val buffer = data.hitboxBuffer.toDouble()
                data.hitbox.forEach { box ->
                    for (i in 0..2) {
                        if (box[0][i] - buffer < minHitbox[i]) minHitbox[i] = maxOf(box[0][i] - buffer, 0.0)
                        if (box[1][i] - buffer < minHitbox[i]) minHitbox[i] = maxOf(box[1][i] - buffer, 0.0)
                        if (box[0][i] + buffer > maxHitbox[i]) maxHitbox[i] = minOf(box[0][i] + buffer, 16.0)
                        if (box[1][i] + buffer > maxHitbox[i]) maxHitbox[i] = minOf(box[1][i] + buffer, 16.0)
                    }
                }

                baseNorth = Block.createCuboidShape(minHitbox[0], minHitbox[1], minHitbox[2], maxHitbox[0], maxHitbox[1], maxHitbox[2])
                baseEast = Block.createCuboidShape(16.0 - maxHitbox[2], minHitbox[1], minHitbox[0], 16.0 - minHitbox[2], maxHitbox[1], maxHitbox[0])
                baseSouth = Block.createCuboidShape(16.0 - maxHitbox[0], minHitbox[1], 16.0 - maxHitbox[2], 16.0 - minHitbox[0], maxHitbox[1], 16.0 - minHitbox[2])
                baseWest = Block.createCuboidShape(minHitbox[2], minHitbox[1], 16.0 - maxHitbox[0], maxHitbox[2], maxHitbox[1], 16.0 - minHitbox[0])

            }else{
                data.hitbox.forEach{box->
                    for (i in 0..2) {

                        if (box[0][i] > box[1][i]) {
                            val temp = box[0][i]
                            box[0][i] = box[1][i]
                            box[1][i] = temp
                        }

                        if (box[0][i] == box[1][i]) {
                            /*println("Thin block detected: " + data.displayName)
                            box[0][i] = box[0][i] - 1;
                            box[1][i] = box[1][i] + 1;*/
                        }
                    }
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

    public fun randomDisplayTick(world: World?, pos: BlockPos?, random: Random?){

        if (!data.particles.enabled) return

        val d: Double = (pos?.x ?: 0) + data.particles.pos[0]
        val e: Double = (pos?.y ?: 0) + data.particles.pos[1]
        val f: Double = (pos?.z ?: 0) + data.particles.pos[2]
        world?.addParticle(data.particles.type, d, e, f, data.particles.vel[0], data.particles.vel[1], data.particles.vel[2])
    }
}