package com.ryytikki.bamo.blocks

import com.ryytikki.bamo.tools.BamoFunctionProvider
import com.ryytikki.bamo.tools.BlockData
import net.minecraft.block.BlockState
import net.minecraft.block.Block
import net.minecraft.block.FurnaceBlock
import net.minecraft.block.HorizontalBlock
import net.minecraft.item.BlockItemUseContext
import net.minecraft.state.DirectionProperty
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.Direction
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.text.IFormattableTextComponent
import net.minecraft.world.IBlockReader

val HORIZONTAL_FACING:DirectionProperty = BlockStateProperties.HORIZONTAL_FACING

class BAMOHorizontalBlock(prop: Properties, data: BlockData) : Block(prop) {

    private val bamoFunc: BamoFunctionProvider

    init {
        bamoFunc = BamoFunctionProvider(this, data)
        this.registerDefaultState(this.stateDefinition.any().setValue(HORIZONTAL_FACING, Direction.NORTH))
    }

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return bamoFunc.getBoundingBoxFromFacing(state.getValue(HORIZONTAL_FACING))
    }

    override fun getName(): IFormattableTextComponent {
        return bamoFunc.getName()
    }

    override fun getDescriptionId(): String {
        return bamoFunc.getDescID()
    }

    override fun getStateForPlacement(context: BlockItemUseContext): BlockState? {
        return this.defaultBlockState().setValue(HORIZONTAL_FACING, context.horizontalDirection.opposite)
    }

    override fun createBlockStateDefinition(builder: StateContainer.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(HORIZONTAL_FACING)
    }

    override fun rotate(state: BlockState, direction: Rotation): BlockState {
        return state.setValue(HORIZONTAL_FACING, direction.rotate(state.getValue(HORIZONTAL_FACING)))
    }

    override fun mirror(state: BlockState, mirrorIn: Mirror): BlockState {
        return state.rotate((mirrorIn.getRotation((state.getValue(HORIZONTAL_FACING)))))
    }

}