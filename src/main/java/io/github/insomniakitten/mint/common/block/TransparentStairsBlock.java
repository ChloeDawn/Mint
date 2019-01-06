package io.github.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.StairShape;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class TransparentStairsBlock extends SimpleStairsBlock {
  private final BlockRenderLayer layer;

  public TransparentStairsBlock(final Block material, final BlockRenderLayer layer) {
    super(material);
    this.layer = layer;
  }

  @Override
  public boolean isSimpleFullBlock(final BlockState state, final BlockView view, final BlockPos position) {
    return false;
  }

  @Override // fixme bad logic
  @Deprecated // isSideInvisible
  public boolean isSideVisible(final BlockState state, final BlockState other, final Direction side) {
    if (this != other.getBlock()) {
      return false;
    }

    final Direction f1 = state.get(Properties.FACING_HORIZONTAL);
    final BlockHalf h1 = state.get(Properties.BLOCK_HALF);
    final StairShape s1 = state.get(Properties.STAIR_SHAPE);
    final Direction f2 = other.get(Properties.FACING_HORIZONTAL);
    final BlockHalf h2 = other.get(Properties.BLOCK_HALF);
    final StairShape s2 = other.get(Properties.STAIR_SHAPE);

    if (side.getAxis().isHorizontal()) {
      if (side == f1.rotateYClockwise() || side == f1.rotateYCounterclockwise()) {
        return h1 == h2 && f1 == f2;
      }
      if (side == f1) {
        return h1 == h2 && side == f2.getOpposite();
      }
      if (side == f1.getOpposite()) {
        if (h1 == h2) {
          if (f1 == f2) {
            return s2 != StairShape.OUTER_RIGHT && s2 != StairShape.OUTER_LEFT;
          }
          if (f1 != f2.getOpposite()) {
            return s2 == StairShape.INNER_RIGHT || s2 == StairShape.INNER_LEFT;
          }
        }
      }
      return f1 != f2 && f1 != f2.getOpposite();
    }
    return Direction.UP == side ? h2 == BlockHalf.BOTTOM : h1 == BlockHalf.TOP;
  }

  @Override
  public boolean isWaterLogged(final BlockState state, final BlockView view, final BlockPos position) {
    return true;
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return this.layer;
  }
}
