package net.insomniakitten.mint.block;

import lombok.val;
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
        val axis = state.getValue(BlockStateProperties.AXIS);
        val materialState = this.material.getDefaultState().withProperty(BlockStateProperties.AXIS, axis);

        return this.material.getMapColor(materialState, reader, position);
    }

    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rotation) {
        if (rotation == Rotation.COUNTERCLOCKWISE_90 || rotation == Rotation.CLOCKWISE_90) {
            val axis = state.getValue(BlockStateProperties.AXIS);

            if (axis.isHorizontal()) {
                return state.withProperty(BlockStateProperties.AXIS, axis == Axis.X ? Axis.Z : Axis.X);
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
        @Nullable val state = super.getStateForPlacement(context);

        if (state == null) {
            return null;
        }

        val other = context.getWorld().getBlockState(context.getPos());

        if (other.getBlock() == this) {
            val axis = other.getValue(BlockStateProperties.AXIS);

            return state.withProperty(BlockStateProperties.AXIS, axis);
        }

        val axis = context.getFace().getAxis();

        return state.withProperty(BlockStateProperties.AXIS, axis);
    }
}
