package net.insomniakitten.mint.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class RotatedSlabBlock extends BlockSlab {
    private final Block material;

    public RotatedSlabBlock(final Block material) {
        super(Builder.from(material));
        this.material = material;
    }

    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockReader reader, final BlockPos position) {
        final Axis axis = state.get(BlockStateProperties.AXIS);
        final IBlockState materialState = this.material.getDefaultState().with(BlockStateProperties.AXIS, axis);

        return this.material.getMapColor(materialState, reader, position);
    }

    @Override
    @Deprecated
    public IBlockState rotate(final IBlockState state, final Rotation rotation) {
        if (Rotation.COUNTERCLOCKWISE_90 == rotation || Rotation.CLOCKWISE_90 == rotation) {
            final Axis axis = state.get(BlockStateProperties.AXIS);

            if (axis.isHorizontal()) {
                return state.with(BlockStateProperties.AXIS, axis == Axis.X ? Axis.Z : Axis.X);
            }
        }

        return state;
    }

    @Override
    protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(BlockStateProperties.AXIS);
    }

    @Override
    @Nullable
    public IBlockState getStateForPlacement(final BlockItemUseContext context) {
        @Nullable final IBlockState state = super.getStateForPlacement(context);

        if (state == null) {
            return null;
        }

        final IBlockState other = context.getWorld().getBlockState(context.getPos());

        if (this == other.getBlock()) {
            final Axis axis = other.get(BlockStateProperties.AXIS);

            return state.with(BlockStateProperties.AXIS, axis);
        }

        final Axis axis = context.getFace().getAxis();

        return state.with(BlockStateProperties.AXIS, axis);
    }
}
