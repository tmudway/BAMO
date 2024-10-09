package com.ryytikki.bamo.blocks

import com.ryytikki.bamo.tools.BamoFunctionProvider
import com.ryytikki.bamo.tools.BlockData
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.text.MutableText
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.*

val HORIZONTAL_FACING:DirectionProperty = Properties.HORIZONTAL_FACING

class BAMOHorizontalBlock(prop: Settings, data: BlockData) : Block(prop){
    private val bamoFunc: BamoFunctionProvider = BamoFunctionProvider(this, data)

    init {
        defaultState = this.stateManager.defaultState.with(Properties.HORIZONTAL_FACING, Direction.NORTH)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.HORIZONTAL_FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        return this.defaultState.with(Properties.HORIZONTAL_FACING, (ctx.horizontalPlayerFacing.opposite?:Direction.NORTH))
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(state: BlockState, worldIn: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape {
        return bamoFunc.getBoundingBoxFromFacing(state.get(HORIZONTAL_FACING))
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