package net.insomniakitten.mint.block;

import lombok.val;
import net.insomniakitten.mint.util.GrassGrowth;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

public class GrassStairsBlock extends SimpleStairsBlock implements SpreadableBlock, IGrowable {
    public GrassStairsBlock() {
        super(Blocks.GRASS_BLOCK);
    }

    @Override
    @Deprecated
    public boolean isSolid(final IBlockState state) {
        return true;
    }

    @Override
    public IItemProvider getItemDropped(final IBlockState state, final World world, final BlockPos position, final int fortune) {
        return Objects.requireNonNull(GrassGrowth.getDirtEquivalent(state), "dirtEquivalent").getBlock();
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public void tick(final IBlockState state, final World world, final BlockPos position, final Random rand) {
        this.onSpreadingTick(state, world, position, rand);
    }

    @Override
    @Nullable
    public IBlockState getStateForPlacement(final BlockItemUseContext context) {
        @Nullable val state = super.getStateForPlacement(context);

        if (state != null && state.get(BlockStateProperties.HALF) == Half.TOP) {
            val up = context.getPos().up();
            val above = context.getWorld().getBlockState(up);

            return state.with(BlockStateProperties.SNOWY, this.isSnowBlock(above));
        }

        return state;
    }

    @Override
    public IBlockState updatePostPlacement(final IBlockState state, final EnumFacing face, final IBlockState other, final IWorld world, final BlockPos position, final BlockPos offset) {
        if (face == EnumFacing.UP && Half.TOP == state.get(BlockStateProperties.HALF)) {
            return state.with(BlockStateProperties.SNOWY, this.isSnowBlock(other));
        }

        return super.updatePostPlacement(state, face, other, world, position, offset);
    }

    @Override
    protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(BlockStateProperties.SNOWY);
    }

    @Override
    public boolean canGrow(final IBlockReader reader, final BlockPos position, final IBlockState state, final boolean isClient) {
        return Half.TOP == state.get(BlockStateProperties.HALF);
    }

    @Override
    public boolean canUseBonemeal(final World world, final Random random, final BlockPos position, final IBlockState state) {
        return Half.TOP == state.get(BlockStateProperties.HALF);
    }

    @Override
    public void grow(final World world, final Random random, final BlockPos position, final IBlockState state) {
        if (Half.BOTTOM == state.get(BlockStateProperties.HALF)) {
            this.onGrow(world, position, random);
        }
    }

    private boolean isSnowBlock(final IBlockState state) {
        val block = state.getBlock();

        return block == Blocks.SNOW_BLOCK || block == Blocks.SNOW;
    }
}
