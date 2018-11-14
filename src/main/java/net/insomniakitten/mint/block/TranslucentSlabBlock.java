package net.insomniakitten.mint.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class TranslucentSlabBlock extends SimpleSlabBlock {
    public TranslucentSlabBlock(final Block material) {
        super(material);
    }

    @Override
    public int getOpacity(final IBlockState state, final IBlockReader reader, final BlockPos position) {
        return Blocks.WATER.getDefaultState().getOpacity(reader, position);
    }

    @Override
    @Deprecated
    public boolean isSideInvisible(final IBlockState state, final IBlockState other, final EnumFacing face) {
        if (this != other.getBlock()) {
            return false;
        }

        final SlabType type = other.get(BlockStateProperties.SLAB_TYPE);

        if (SlabType.DOUBLE == type) {
            return true;
        }

        if (face.getAxis().isVertical()) {
            return face == (SlabType.TOP == type ? EnumFacing.DOWN : EnumFacing.UP);
        }

        return state.get(BlockStateProperties.SLAB_TYPE) == type;
    }

    @Override
    public boolean func_200123_i(final IBlockState state, final IBlockReader reader, final BlockPos position) {
        return true;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
