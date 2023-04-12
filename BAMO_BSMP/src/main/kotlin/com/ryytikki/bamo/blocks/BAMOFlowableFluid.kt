import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FluidBlock
import net.minecraft.fluid.FlowableFluid
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.tag.FluidTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import java.util.*

/*abstract class BAMOFlowableFluid(bucket: Item, still: FlowableFluid flowing: FlowableFluid) : FlowableFluid() {
    override fun getFlowing(): Fluid {
        return flowing
    }

    override fun getStill(): Fluid {
        return still
    }

    override fun getBucketItem(): Item {
        return ModItems.HONEY_BUCKET
    }

    public override fun randomDisplayTick(world: World, pos: BlockPos, state: FluidState, random: Random) {
        if (!state.isStill && !state.get(FALLING)) {
            if (random.nextInt(64) == 0) {
                world.playSound(
                    pos.x.toDouble() + 0.5,
                    pos.y.toDouble() + 0.5, pos.z.toDouble() + 0.5,
                    SoundEvents.BLOCK_HONEY_BLOCK_SLIDE, SoundCategory.BLOCKS,
                    random.nextFloat() * 0.25f + 0.75f, random.nextFloat() + 0.5f,
                    false
                )
            }
        } else if (random.nextInt(10) == 0) {
            world.addParticle(
                ParticleTypes.UNDERWATER, pos.x.toDouble() + random.nextDouble(),
                pos.y.toDouble() + random.nextDouble(),
                pos.z.toDouble() + random.nextDouble(),
                0.0, 0.0, 0.0
            )
        }
    }

    public override fun getParticle(): ParticleEffect? {
        return ParticleTypes.DRIPPING_WATER
    }

    override fun isInfinite(): Boolean {
        return false
    }

    override fun beforeBreakingBlock(world: WorldAccess, pos: BlockPos, state: BlockState) {
        val blockEntity = if (state.hasBlockEntity()) world.getBlockEntity(pos) else null
        Block.dropStacks(state, world, pos, blockEntity)
    }

    public override fun getFlowSpeed(world: WorldView): Int {
        return 4
    }

    public override fun toBlockState(state: FluidState): BlockState {
        return ModBlocks.HONEY_FLUID_BLOCK.getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(state))
    }

    override fun matchesType(fluid: Fluid): Boolean {
        return fluid === still || fluid === flowing
    }

    public override fun getLevelDecreasePerBlock(world: WorldView): Int {
        return 1
    }

    override fun getTickRate(world: WorldView): Int {
        return 5
    }

    public override fun canBeReplacedWith(
        state: FluidState,
        world: BlockView,
        pos: BlockPos,
        fluid: Fluid,
        direction: Direction
    ): Boolean {
        return direction == Direction.DOWN && !fluid.isIn(FluidTags.WATER)
    }

    override fun getBlastResistance(): Float {
        return 100.0f
    }

    override fun getBucketFillSound(): Optional<SoundEvent> {
        return Optional.of(SoundEvents.ITEM_BUCKET_FILL)
    }

    class Flowing : BAMOFlowableFluid() {
        override fun appendProperties(builder: StateManager.Builder<Fluid, FluidState>) {
            super.appendProperties(builder)
            builder.add(LEVEL)
        }

        override fun getLevel(state: FluidState): Int {
            return state.get(LEVEL) as Int
        }

        override fun isStill(state: FluidState): Boolean {
            return false
        }
    }

    class Still : BAMOFlowableFluid() {
        override fun getLevel(state: FluidState): Int {
            return 8
        }

        override fun isStill(state: FluidState): Boolean {
            return true
        }
    }
}*/