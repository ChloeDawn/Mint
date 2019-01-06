package io.github.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
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
  @Deprecated
  public boolean isFullBoundsCubeForCulling(final BlockState state) {
    return true;
  }

  @Override
  @Deprecated
  public boolean isSideVisible(final BlockState state, final BlockState neighbor, final Direction side) {
    final Block block = neighbor.getBlock();
    if (block == this.getMaterial()) {
      return true;
    }

    if (block instanceof TransparentSlabBlock && this.getMaterial() == ((SimpleSlabBlock) block).getMaterial()) {
      final SlabType type = neighbor.get(Properties.SLAB_TYPE);

      if (SlabType.DOUBLE == type) {
        return true;
      }

      if (side.getAxis().isVertical()) {
        return side == (SlabType.TOP == type ? Direction.DOWN : Direction.UP);
      }

      if (side.getOpposite() == state.get(Properties.FACING_HORIZONTAL)) {
        return (SlabType.TOP == type) == (BlockHalf.TOP == state.get(Properties.BLOCK_HALF));
      }
    }

    return false;
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
