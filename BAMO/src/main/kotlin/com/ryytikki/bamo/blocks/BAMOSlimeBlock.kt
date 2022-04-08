package com.ryytikki.bamo.blocks

import com.ryytikki.bamo.tools.BamoFunctionProvider
import com.ryytikki.bamo.tools.BlockData
import com.ryytikki.bamo.tools.initBlockProperties
import net.minecraft.block.Block
import net.minecraft.block.SlimeBlock
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import kotlin.math.abs

class BAMOSlimeBlock(prop: Properties, data: BlockData) : SlimeBlock(prop){

    private val bamoFunc:BamoFunctionProvider

    init {
        bamoFunc = BamoFunctionProvider(this, data)
    }

    override fun fallOn(worldIn:World, pos:BlockPos, entityIn: Entity, fallDistance:Float){
        if (entityIn.isSuppressingBounce){
            super.fallOn(worldIn, pos, entityIn, fallDistance)
        }else{
            entityIn.causeFallDamage(fallDistance, 0.0F)
        }
    }

    override fun updateEntityAfterFallOn(worldIn: IBlockReader, entityIn: Entity){
        if (entityIn.isSuppressingBounce){
            super.updateEntityAfterFallOn(worldIn, entityIn)
        }else{
            this.bounce(entityIn)
        }
    }

    private fun bounce(entity: Entity){
        val vec3d: Vector3d = entity.deltaMovement
        if (vec3d.y < 0.0){
            val d0:Double = if(entity is LivingEntity) 0.8 else 1.0
            entity.setDeltaMovement(vec3d.x, -1 * vec3d.y * d0, vec3d.z)
        }
    }

    override fun stepOn(worldIn: World, pos: BlockPos, entityIn: Entity){
        val d0:Double = abs(entityIn.deltaMovement.y)
        if (d0 < 0.1 && !entityIn.isSteppingCarefully){
            val d1:Double = 0.4 + d0 * 0.2
            entityIn.deltaMovement = entityIn.deltaMovement.multiply(d1, 1.0, d1)
        }
        super.stepOn(worldIn, pos,entityIn)
    }
}