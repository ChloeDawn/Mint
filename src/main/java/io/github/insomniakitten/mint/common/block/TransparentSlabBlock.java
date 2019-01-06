package io.github.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.SlabType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class TransparentSlabBlock extends SimpleSlabBlock {
  private final BlockRenderLayer layer;

  public TransparentSlabBlock(final Block material, final BlockRenderLayer layer) {
    super(material);
    this.layer = layer;
  }

  @Override
  @Deprecated
  public boolean isSideVisible(final BlockState state, final BlockState neighbor, final Direction side) {
    if (this != neighbor.getBlock()) {
      return false;
    }

    final SlabType type = neighbor.get(Properties.SLAB_TYPE);

    if (SlabType.DOUBLE == type) {
      return true;
    }

    if (side.getAxis().isVertical()) {
      return side == (SlabType.TOP == type ? Direction.DOWN : Direction.UP);
    }

    return state.get(Properties.SLAB_TYPE) == type;
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
