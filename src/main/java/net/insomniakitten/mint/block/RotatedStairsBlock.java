package net.insomniakitten.mint.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockStairs;
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

public class RotatedStairsBlock extends BlockStairs {
    private final Block material;

    public RotatedStairsBlock(final Block material) {
        super(material.getDefaultState(), Builder.from(material));
        this.material = material;
    }

    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockReader reader, final BlockPos position) {
        final Axis axis = state.get(BlockRotatedPillar.AXIS);
        final IBlockState materialState = this.material.getDefaultState().with(BlockRotatedPillar.AXIS, axis);

        return this.material.getMapColor(materialState, reader, position);
    }

    @Override
    @Nullable
    public IBlockState getStateForPlacement(final BlockItemUseContext context) {
        @Nullable final IBlockState state = super.getStateForPlacement(context);

        if (state == null) {
            return null;
        }

        final Axis axis = context.getFace().getAxis();

        return state.with(BlockStateProperties.AXIS, axis);
    }

    @Override
    @Deprecated
    public IBlockState rotate(final IBlockState state, final Rotation rotation) {
        if (Rotation.COUNTERCLOCKWISE_90 == rotation || Rotation.CLOCKWISE_90 == rotation) {
            final Axis axis = state.get(BlockRotatedPillar.AXIS);

            if (axis.isHorizontal()) {
                return state.with(BlockRotatedPillar.AXIS, axis == Axis.X ? Axis.Z : Axis.X);
            }
        }

        return state;
    }

    @Override
    protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(BlockStateProperties.AXIS);
    }
}

