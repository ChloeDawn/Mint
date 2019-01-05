package io.github.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.SlabType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class TranslucentSlabBlock extends SimpleSlabBlock {
  public TranslucentSlabBlock(final Block material) {
    super(material);
  }

  @Override
  public boolean isSimpleFullBlock(final BlockState state, final BlockView view, final BlockPos position) {
    return false;
  }

  @Override
  @Deprecated // isSideInvisible
  public boolean isSideVisible(final BlockState state, final BlockState other, final Direction face) {
    if (this != other.getBlock()) {
      return false;
    }

    final SlabType type = other.get(Properties.SLAB_TYPE);

    if (SlabType.DOUBLE == type) {
      return true;
    }

    if (face.getAxis().isVertical()) {
      return face == (SlabType.TOP == type ? Direction.DOWN : Direction.UP);
    }

    return state.get(Properties.SLAB_TYPE) == type;
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
