package net.insomniakitten.mint.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class TranslucentPillarBlock extends SimplePillarBlock {
    public TranslucentPillarBlock(final Block material) {
        super(material);
    }

    @Override
    @Deprecated
    public boolean isSideInvisible(final IBlockState state, final IBlockState other, final EnumFacing face) {
        return other.getBlock() == this;
    }

    @Override
    public boolean func_200123_i(final IBlockState state, final IBlockReader reader, final BlockPos position) {
        return true;
    }

    @Override
    @Deprecated
    public int getOpacity(final IBlockState state, final IBlockReader reader, final BlockPos position) {
        return Blocks.WATER.getDefaultState().getOpacity(reader, position);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
