package net.insomniakitten.mint.block;

import lombok.val;
import net.insomniakitten.mint.util.GrassGrowth;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
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

public class GrassSlabBlock extends SimpleSlabBlock implements SpreadableBlock, IGrowable {
    public GrassSlabBlock() {
        super(Blocks.GRASS_BLOCK);
        this.setDefaultState();
    }

    @Override
    @Deprecated
    public boolean isSolid(final IBlockState state) {
        return true;
    }

    @Override
    @Deprecated
    public void tick(final IBlockState state, final World world, final BlockPos position, final Random rand) {
        this.onSpreadingTick(state, world, position, rand);
    }

    @Override
    public IItemProvider getItemDropped(final IBlockState state, final World world, final BlockPos position, final int fortune) {
        return Objects.requireNonNull(GrassGrowth.getDirtEquivalent(state), "dirtEquivalent").getBlock();
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    protected ItemStack getSilkTouchDrop(final IBlockState state) {
        if (SlabType.DOUBLE == state.getValue(BlockStateProperties.SLAB_TYPE)) {
            return new ItemStack(this, 2);
        }

        return new ItemStack(this);
    }

    @Override
    public boolean canGrow(final IBlockReader reader, final BlockPos position, final IBlockState state, final boolean isClient) {
        return SlabType.BOTTOM != state.getValue(BlockStateProperties.SLAB_TYPE);
    }

    @Override
    public boolean canUseBonemeal(final World world, final Random random, final BlockPos position, final IBlockState state) {
        return SlabType.BOTTOM != state.getValue(BlockStateProperties.SLAB_TYPE);
    }

    @Override
    public void grow(final World world, final Random random, final BlockPos position, final IBlockState state) {
        if (SlabType.BOTTOM != state.getValue(BlockStateProperties.SLAB_TYPE)) {
            this.onGrow(world, position, random);
        }
    }

    @Override
    protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(BlockStateProperties.SNOWY);
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    @Nullable
    public IBlockState getStateForPlacement(final BlockItemUseContext context) {
        @Nullable val state = super.getStateForPlacement(context);

        if (state != null && SlabType.BOTTOM != state.getValue(BlockStateProperties.SLAB_TYPE)) {
            val up = context.getPos().up();
            val above = context.getWorld().getBlockState(up);

            return state.withProperty(BlockStateProperties.SNOWY, this.isSnowBlock(above));
        }

        return state;
    }

    @Override
    public IBlockState updatePostPlacement(final IBlockState state, final EnumFacing face, final IBlockState other, final IWorld world, final BlockPos position, final BlockPos offset) {
        if (face == EnumFacing.UP && SlabType.BOTTOM != state.getValue(BlockStateProperties.SLAB_TYPE)) {
            return state.withProperty(BlockStateProperties.SNOWY, this.isSnowBlock(other));
        }

        return super.updatePostPlacement(state, face, other, world, position, offset);
    }

    private boolean isSnowBlock(final IBlockState state) {
        val block = state.getBlock();

        return block == Blocks.SNOW_BLOCK || block == Blocks.SNOW;
    }

    private void setDefaultState() {
        val stateContainer = this.getStateContainer();
        val baseState = stateContainer.getBaseState();

        this.setDefaultState(baseState.withProperty(BlockStateProperties.SNOWY, false));
    }
}
