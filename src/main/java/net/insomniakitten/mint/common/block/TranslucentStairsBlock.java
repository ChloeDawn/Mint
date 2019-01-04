package net.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class TranslucentStairsBlock extends SimpleStairsBlock {
  public TranslucentStairsBlock(final Block material) {
    super(material);
  }

  @Override
  public boolean isSimpleFullBlock(final BlockState state, final BlockView view, final BlockPos position) {
    return false;
  }

  @Override // fixme bad logic
  @Deprecated // isSideInvisible
  public boolean isSideVisible(final BlockState state, final BlockState other, final Direction face) {
    if (this != other.getBlock()) {
      return false;
    }

    final Direction facing = state.get(Properties.FACING_HORIZONTAL);
    final Direction otherFacing = other.get(Properties.FACING_HORIZONTAL);

    if (face == Direction.UP) {
      final BlockHalf half = other.get(Properties.BLOCK_HALF);

      return BlockHalf.TOP != half || otherFacing == facing;
    }

    if (otherFacing == facing.getOpposite()) {
      return face == facing;
    }

    if (face.getAxis().isHorizontal()) {
      return otherFacing == face.rotateYClockwise() || otherFacing == face.rotateYCounterclockwise();
    }

    return false;
  }

  @Override
  public boolean isWaterLogged(final BlockState state, final BlockView view, final BlockPos position) {
    return true;
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }
}
